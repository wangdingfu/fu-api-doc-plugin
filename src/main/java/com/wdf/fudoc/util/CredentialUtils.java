package com.wdf.fudoc.util;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;

/**
 * @author wangdingfu
 * @date 2022-08-18 14:07:16
 */
public class CredentialUtils {

    private static CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName("Fu Doc", key)
        );
    }


    public static String getPassword(String key) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(key);

        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            return credentials.getPasswordAsString();
        }
        return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }


    public static void save(String key,String content){
        CredentialAttributes credentialAttributes = createCredentialAttributes(content); // see previous sample
        Credentials credentials = new Credentials("Fu Doc", getPassword(key));
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }
}
