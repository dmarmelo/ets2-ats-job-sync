%%{
    machine siiParserText;

	action unitClassStart {
		unitClassStart = p;
	}

	action unitClassEnd {
		unitClass = new String(Arrays.copyOfRange(data, unitClassStart, p));
	}

	action unitInstanceStart {
		unitInstanceStart = p;
	}

	action unitInstanceEnd {
        unitInstance = new String(Arrays.copyOfRange(data, unitInstanceStart, p));
	}

	action unitStart {
        callback.apply(Context.UNIT_START, unitClass, unitInstance, attrSourceValue, p - start);
	}

	action unitEnd {
        callback.apply(Context.UNIT_END, unitClass, unitInstance, attrSourceValue, p - start);
	}

	action attrNameStart {
		attrNameStart = p;
	}

	action attrNameEnd {
        attrName = new String(Arrays.copyOfRange(data, attrNameStart, p));
	}

	action attrValueStart {
		attrRawValue = "";
		attrSourceValueStart = p;
	}

	action attrValueEnd {
        attrSourceValue = new String(Arrays.copyOfRange(data, attrSourceValueStart, p));
        callback.apply(Context.ATTRIBUTE, attrName, attrRawValue, attrSourceValue, p - start);
	}

	action attrBareValueStart {
		attrBareValueStart = p;
	}

	action attrBareValueEnd {
        attrRawValue = new String(Arrays.copyOfRange(data, attrBareValueStart, p));
	}

	action attrQuotedValueRawStart {
		attrQuotedValueRawStart = p;
	}

	action attrQuotedValueRawEnd {
        attrRawValue += new String(Arrays.copyOfRange(data, attrQuotedValueRawStart, p));
	}

	action attrQuotedValueEscapedChar {
        attrRawValue += (char)data[p - 1];
	}

	action attrQuotedValueEscapedHex {
        attrRawValue += (((data[p - 2] & 0x40) != 0 ? 9 : 0) + (data[p - 2] & 0x0F)) << 4 | (((data[p - 1] & 0x40) != 0 ? 9 : 0) + (data[p - 1] & 0x0F));
	}

	action success {
		success = true;
	}

	h_space = [ \t];
	identifier = (alpha | digit | '_')+;

	array_index = '[' . digit+ . ']';
	attribute_name = (identifier . array_index?) >attrNameStart %attrNameEnd;
	escape_hex = 'x' . xdigit{2};
	escape_seq = '\\' . (escape_hex %attrQuotedValueEscapedHex | [\\"] %attrQuotedValueEscapedChar);
	quoted_raw_chars = (print - [\\"])+ >attrQuotedValueRawStart %attrQuotedValueRawEnd;
	quoted_value = '"' . ('' | (quoted_raw_chars | escape_seq)**) . '"';
	bare_value = (print - ["])+ >attrBareValueStart %attrBareValueEnd;
	value = ((quoted_value . h_space*) | bare_value) >attrValueStart %attrValueEnd;

	attribute = (attribute_name . ':' . h_space+ . value . [\r\n]);

	unit_class = identifier >unitClassStart %unitClassEnd;
	unit_instance_name = (identifier . ('.' identifier)*) >unitInstanceStart %unitInstanceEnd;
	unit_head = (unit_class . h_space+ . ':' . h_space+ . unit_instance_name . h_space+ . '{') %unitStart;
	unit = unit_head . (space* . attribute)* . space* . '}' %unitEnd;

	wrapper = "SiiNunit" . space+ . '{' . (space* . unit . space*)+ . '}' %success;

	file = wrapper . space*;

	main := file;
}%%

package pt.marmelo.ets2atsjobsync.parser;

import java.util.*;

public class SiiTextParser {
    %% write data;
    public static boolean parse(byte[] data, ParseCallback callback) {
        // Data Pointer
		int p;
		// Data End Pointer
		int pe;
		// End Of File Pointer
		int eof;
		// Current State
		int cs;
        // Other Variables
        int te, ts, act;

        int start = 0;
    
        %% write init;

        p = start;
        pe = start + data.length;
        eof = pe;

        String unitClass = "";
		int unitClassStart = -1;
		String unitInstance = "";
		int unitInstanceStart = -1;
		String attrName = "";
		int attrNameStart = -1;
		String attrRawValue = "";
		int attrBareValueStart = -1;
		int attrQuotedValueRawStart = -1;
		String attrSourceValue = "";
		int attrSourceValueStart = -1;

		boolean success = false;

        %% write exec;

        return success;
    }
}