/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobilewallet.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.commons.codec.binary.Base64;
import android.content.Context;
import com.mobilewallet.R;

/**
 * 
 * @author gopi
 */
public class EncryptionUtil {

	private Cipher cipher;

	public EncryptionUtil(String passPhrase, Context context) {
		try {

			int iterationCount = context.getResources().getInteger(
					R.integer.redeem_world_wide_padding);
			byte[] salt = new byte[] {
					(byte) context.getResources().getInteger(
							R.integer.actionbar_coins_text_padding),
					(byte) context.getResources().getInteger(
							R.integer.reg_failed_layout_padding),
					(byte) context.getResources().getInteger(
							R.integer.forgot_pwd_button_text_padding),
					(byte) context.getResources().getInteger(
							R.integer.email_cfm_butt_left_padding),
					(byte) context.getResources().getInteger(
							R.integer.main_text_padding),
					(byte) context.getResources().getInteger(
							R.integer.update_referrer_invited_butt_padding),
					(byte) context.getResources().getInteger(
							R.integer.paypal_success_heading_padding),
					(byte) context.getResources().getInteger(
							R.integer.paypal_instructions_padding) };

			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(
							new PBEKeySpec(passPhrase.toCharArray(), salt,
									iterationCount));
			cipher = Cipher.getInstance(key.getAlgorithm());

			cipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt,
					iterationCount));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encryptURLSafe(String str) {

		try {
			return (new String(Base64.encodeBase64(cipher.doFinal(str
					.getBytes("UTF8"))), "UTF8")).replace("+", "-")
					.replace("/", "_").replace("=", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}