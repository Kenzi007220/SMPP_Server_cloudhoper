package mock_smpp_server.util;

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;

public class DataCodingToCharsetUtil {

    public static Charset getCharacterSet(int code) {

        switch (code) {
            case 0:
                return CharsetUtil.CHARSET_ISO_8859_1;
            case 4:
                return CharsetUtil.CHARSET_GSM8;
            case 8:
                return CharsetUtil.CHARSET_UCS_2;
            default:
                return CharsetUtil.CHARSET_GSM;
        }

    }


}
