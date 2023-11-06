package com.huii.common.constants;

/**
 * RegEX 正则表达式
 *
 * @author huii
 */
public interface RegConstants {

    String XSS_MATCHER = "<(\\S*?)[^>]*>.*?|<.*? />";

    String EMAIL_MATCHER = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    String PHONE_MATCHER = "^1\\d{10}$";

    String ID_CARD_MATCHER = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|(X|x))";

    String PASSWORD_MATCHER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
}
