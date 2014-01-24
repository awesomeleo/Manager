package com.example.manager.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.R;
import com.example.manager.service.UserService;
import com.example.manager.util.DialogFactory;
import com.example.manager.vo.User;

public class RegisterActivity extends Activity {

	private Button registerBtn;
	private Button returnBtn;
	private Dialog dialog = null;
	
	private EditText userName;
	private EditText password;
	private EditText passwordCon;
	private EditText email;
	
	//�ж��û����Ƿ�ͨ����֤
	private boolean userNamePass = false;
	//�ж������Ƿ�ͨ����֤
	private boolean passwordPass = false;
	//�ж������Ƿ�ͨ����֤
	private boolean emailPass = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		registerBtn = (Button) findViewById(R.id.reg_btn);
		returnBtn=(Button) findViewById(R.id.Button01);
		userName = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		passwordCon = (EditText) findViewById(R.id.passwordcon);
		email = (EditText) findViewById(R.id.email);
		
		final UserService userService = new UserService(RegisterActivity.this);
		
		userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!userName.hasFocus()){
					if(checkEmail(userName.getText().toString().trim())){
						if(userService.registerName(userName.getText().toString().trim())){
							userNamePass = true;
						}else{
							Toast.makeText(RegisterActivity.this, "���û����Ѵ��ڣ���ע�������û���", 
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});
		
		password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!password.hasFocus()){
					if(checkPassword(password.getText().toString().trim())){
						passwordPass = true;
					}
				}
			}
		});
		
		passwordCon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!passwordCon.hasFocus()){
					if(!checkPasswordEqual(password.getText().toString().trim(),
							passwordCon.getText().toString().trim())){
						passwordPass = false;
						password.setText("");
						passwordCon.setText("");
					}else{
						passwordPass = true;
					}
				}
			}
		});
			
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkUserName(email.getText().toString().trim())){
					emailPass = true;
				}
				if(checkEmpty()&&userNamePass&&passwordPass&&emailPass){
					showRequestDialog();
					String userNameStr = userName.getText().toString().trim();
					String passwordStr = password.getText().toString().trim();
					String emailStr = email.getText().toString().trim();
					
					
					User user = new User();
					user.setUserName(userNameStr);
					user.setUserPwd(passwordStr);
					user.setUserEmail(emailStr);
					
					if(userService.register(user)){
						dialog.cancel();
						//new AlertDialog.Builder(RegisterActivity.this)
						//.setMessage("��ϲ��ע��ɹ������ȷ����ת����½����")
						//.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							//@Override
							//public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setClass(RegisterActivity.this, UserListActivity.class);
								startActivity(intent);
								finish();
								//dialog.cancel();
							//}
						//}).setCancelable(false).create().show();
					}
				}
			}
		});
		returnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void showRequestDialog() {
		if (dialog != null) {
			dialog.dismiss();// ��ʹ˶Ի���
			dialog = null;
		}
		dialog = DialogFactory.creatRequestDialog(this, "����ע����...");
		dialog.show();
	}
	
	
	/************************************��֤����*************************************/
	//��֤�û�����ʽ
	private boolean checkUserName(String username){
		if(username.matches("(\\w){6,18}")){
			return true;
		}else{
			Toast.makeText(RegisterActivity.this, "�ǳ�Ϊ6-18���ַ�����ʹ�����֡���ĸ���»���", 
					Toast.LENGTH_LONG).show();
		}
		return false;
	}
	//��֤�����ʽ
	private boolean checkPassword(String password){
		if(password.matches("(\\w){6,15}")){
			return true;
		}else{
			Toast.makeText(RegisterActivity.this, "����Ϊ6-15���ַ������ִ�Сд,��ʹ�����֡���ĸ���»���", 
					Toast.LENGTH_LONG).show();
		}
		return false;
	}
	//��֤�����ʽ
	private boolean checkEmail(String email){
		if(email.matches("^[a-zA-Z0-9_]+@([a-zA-Z0-9]+\\.)+(com|cn|net)$")){
			return true;
		}else{
			Toast.makeText(RegisterActivity.this, "���������ʽ����ȷ����������ȷ������", 
					Toast.LENGTH_LONG).show();
		}
		return false;
	}
	//��֤�������������Ƿ���ͬ
	private boolean checkPasswordEqual(String password,String passwordCon){
		if(password.equals(passwordCon)){
			return true;
		}else{
			Toast.makeText(RegisterActivity.this, "������������벻һ�£�����������", 
					Toast.LENGTH_LONG).show();
		}
		return false;
	}
	//�ж��Ƿ��п�����
	private boolean checkEmpty(){
		if(userName.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "�û���Ϊ�գ��������û���", 
					Toast.LENGTH_LONG).show();
			return false;
		}
		if(password.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "����Ϊ�գ�����������", 
					Toast.LENGTH_LONG).show();
			return false;
		}
		if(passwordCon.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "ȷ������Ϊ�գ�������ȷ������", 
					Toast.LENGTH_LONG).show();
			return false;
		}
		if(email.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "����Ϊ�գ�����������", 
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}
