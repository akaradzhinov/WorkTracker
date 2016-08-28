package bg.sofia.tu.utils;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 27/08/2016 @ 18:17.
 */
public class ValidatorUtils {

    public static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
