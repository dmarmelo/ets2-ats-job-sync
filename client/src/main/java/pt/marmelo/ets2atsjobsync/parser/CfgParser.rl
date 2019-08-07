%%{
	machine cfgParser;

	action nameStart {
		varNameStart = p;
	}

	action nameEnd {
        varName = new String(Arrays.copyOfRange(data, varNameStart, p));
	}

	action valueRawStart {
		varValueRawStart = p;
	}

	action valueRawEnd {
        varValue += new String(Arrays.copyOfRange(data, varValueRawStart, p));
	}

	action valueEscapedChar {
        varValue += (char)data[p - 1];
	}

	action valueEscapedHex {
        varValue += (((data[p - 2] & 0x40) != 0 ? 9 : 0) + (data[p - 2] & 0x0F)) << 4 | (((data[p - 1] & 0x40) != 0 ? 9 : 0) + (data[p - 1] & 0x0F));
	}

	text_char = print;
	comment = '#' . any*;
	whitespace = [ \t];
	varname = (alpha . (alpha | digit | '_')*) >nameStart %nameEnd;
	escape_hex = 'x' . xdigit{2};
	escape_seq = '\\' . (escape_hex %valueEscapedHex | text_char %valueEscapedChar);
	value_raw_chars = (text_char - [\\"])+ >valueRawStart %valueRawEnd;
	varvalue = ('"' . ('' | ( escape_seq | value_raw_chars)**) . '"');
	uset = ("uset" . whitespace+ . varname . whitespace+ . varvalue . whitespace* . comment?);
	line = whitespace* . uset;

    main := line;
}%%

package pt.marmelo.ets2atsjobsync.parser;

import java.util.*;

public class CfgParser {
    %% write data;
    public static void parse(byte[] data, ParseCallback callback) {
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

        int bufferStart = 0;
		int bufferEnd = bufferStart + data.length;
		int lineStart = bufferStart;
		int lineEnd = -1;
		int varNameStart = -1;
		int varValueRawStart = -1;
		byte[] line;
		String varName;
		String varValue;
		String empty = "";

        while(lineStart < bufferEnd) {
            lineEnd = lineStart;
            while (lineEnd < bufferEnd && data[lineEnd] != '\r' && data[lineEnd] != '\n') {
                ++lineEnd;
            }

            line = Arrays.copyOfRange(data, lineStart, lineEnd);
            p = lineStart;
            pe = lineEnd;
            eof = pe;
            varName = "";
			varValue = "";

            %% write init;
            %% write exec;

            if (cs >= cfgParser_first_final) {
				callback.apply(Context.CFG_PROPERTY, varName, varValue, new String(line), p - bufferStart);
			} else {
				callback.apply(Context.EMPTY_LINE, empty, empty, new String(line), p - bufferStart);
			}

            lineStart = lineEnd;
            if (lineStart < bufferEnd && data[lineStart] == '\r') {
				++lineStart;
			}
			if (lineStart < bufferEnd && data[lineStart] == '\n') {
				++lineStart;
			}
        }
    }
}