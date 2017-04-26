package com.itpoints.njmetro.ui;

import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.UserBean;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.CircleImageView;

/**
 * 个人信息
 * 
 * @author peidongxu
 * 
 */
public class UserInfoUI extends BaseUI implements OnClickListener {
	private CircleImageView iv_user_pic;// 头像
	private TextView tv_name;// 昵称
	private TextView tv_sex;// 性别
	private TextView tv_email;// 邮箱
	private TextView tv_phone;// 手机号
	// 头像,昵称,电话,性别,出生年月日,地区,个性签名,电话短号,qq,电子邮件
	private String mPic, mName, mPhone, mSex, mEmail, mCode;

	// 角色
	private TextView tv_rule;
	private ImageView iv_rule;

	// 数据源
	private UserBean userInfo;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.user_info);
	}

	@Override
	protected void findView_AddListener() {
		iv_user_pic = (CircleImageView) findViewById(R.id.iv_user_pic);
		iv_user_pic.setOnClickListener(this);
		RelativeLayout rl_name = (RelativeLayout) findViewById(R.id.rl_user_info_name);
		rl_name.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_user_info_name);
		RelativeLayout rl_sex = (RelativeLayout) findViewById(R.id.rl_user_info_sex);
		rl_sex.setOnClickListener(this);
		tv_sex = (TextView) findViewById(R.id.tv_user_info_sex);
		RelativeLayout rl_user_info_pass = (RelativeLayout) findViewById(R.id.rl_user_info_pass);
		rl_user_info_pass.setOnClickListener(this);
		RelativeLayout rl_email = (RelativeLayout) findViewById(R.id.rl_user_info_email);
		rl_email.setOnClickListener(this);
		tv_email = (TextView) findViewById(R.id.tv_user_info_email);
		RelativeLayout rl_phone = (RelativeLayout) findViewById(R.id.rl_user_info_phone);
		rl_phone.setOnClickListener(this);
		tv_phone = (TextView) findViewById(R.id.tv_user_info_phone);

		RelativeLayout rl_rule = (RelativeLayout) findViewById(R.id.rl_user_info_rule);
		rl_rule.setOnClickListener(this);
		tv_rule = (TextView) findViewById(R.id.tv_user_info_rule);
		iv_rule = (ImageView) findViewById(R.id.iv_user_info_rule);

		TextView tv_logout = (TextView) findViewById(R.id.tv_user_info_logout);
		tv_logout.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("用户信息");
	}

	@Override
	protected void onResume() {
		super.onResume();
		setValue();
	}

	@Override
	protected void onMyClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_user_pic:
			// 头像
			startActivity(new Intent(this, SetPicUI.class));
			break;
		case R.id.rl_user_info_sex:
			// 性别
			startActivity(new Intent(this, SetSexUI.class));
			break;
		case R.id.rl_user_info_name:
			// 昵称
			startActivity(new Intent(this, SetNameUI.class));
			break;
		case R.id.rl_user_info_email:
			// 邮箱
			startActivity(new Intent(this, SetEmailUI.class));
			break;
		case R.id.rl_user_info_phone:
			// 手机号
			if (!"A".equals(mCode)) {
				intent = new Intent(this, SetPhoneUI.class);
				intent.putExtra("phone", mPhone);
				startActivity(intent);
			}
			break;
		case R.id.rl_user_info_pass:
			// 密码
			startActivity(new Intent(this, UpdatePassUI.class));
			break;
		case R.id.rl_user_info_rule:
			// 修改角色
			intent = new Intent(this, RuleSelectUI.class);
			intent.putExtra("isUpdate", true);
			startActivity(intent);
			break;
		case R.id.tv_user_info_logout:
			MyApplication.token = "";
			MyApplication.userBean = null;
			MyConfig.saveToken(this, "");
			MyConfig.saveUserInfo(this, null);
			MyApplication.getInstance().showToast("注销成功");
			back();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置数据
	 */
	private void setValue() {
		userInfo = MyApplication.userBean;
		Map<String, String> info = userInfo.getInfo();
		if (info == null) {
			return;
		}
		String img_url = "";
		if (userInfo.getConstant() != null) {
			img_url = userInfo.getConstant().get("resourceServer");
		}

		mPic = info.get("avatar");
		mName = info.get("attrib02");
		mSex = info.get("sex");
		mPhone = info.get("loginId");
		mEmail = info.get("attrib32");
		mCode = info.get("code");
		// 头像
		if (!Utils.isEmity(mPic) && mPic.contains("http")) {
			BitmapHelp.getInstance(this).display(iv_user_pic, mPic);
		} else {
			BitmapHelp.getInstance(this).display(iv_user_pic, img_url + mPic);
		}
		// 姓名
		if (!Utils.isEmity(mName)) {
			tv_name.setText(mName);
		}
		// 性别 1男2女
		if ("0".equals(mSex) || "1".equals(mSex)) {
			tv_sex.setText("男");
		} else if ("2".equals(mSex)) {
			tv_sex.setText("女");
		}
		// 邮件
		if (!Utils.isEmity(mEmail)) {
			tv_email.setText(mEmail);
		}
		// 手机号
		if (!Utils.isEmity(mPhone)) {
			tv_phone.setText(mPhone);
		}

		int rule_id = MyConfig.getConfig(this, "config", "rule_id", 0);

		int[] ruleResId = getRuleResId();

		for (int i = 0; i < ruleResId.length; i++) {
			if (rule_id == ruleResId[i]) {
				tv_rule.setText(getRuleName()[i]);
				iv_rule.setBackgroundResource(getRuleResIconId()[i]);
			}
		}

	}

	private String[] getRuleName() {
		return new String[] { "老年人", "学生", "上班族", "本地居民", "游客" };
	}

	private int[] getRuleResId() {
		return new int[] { R.drawable.rule_laoren, R.drawable.rule_stuent, R.drawable.rule_shangban, R.drawable.rule_juming, R.drawable.rule_youke };
	}

	private int[] getRuleResIconId() {
		return new int[] { R.drawable.rule_laoren_icon, R.drawable.rule_stuent_icon, R.drawable.rule_shangban_icon, R.drawable.rule_juming_icon, R.drawable.rule_youke_icon };
	}

	@Override
	protected void back() {
		finish();
	}

}
