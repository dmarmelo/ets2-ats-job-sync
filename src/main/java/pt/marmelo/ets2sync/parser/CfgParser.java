
// line 1 "CfgParser.rl"

// line 40 "CfgParser.rl"


package pt.marmelo.ets2sync.parser;

import java.util.*;

public class CfgParser {
    
// line 14 "CfgParser.java"
private static byte[] init__cfgParser_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    3,    1,    4,    1,
	    5,    2,    4,    2,    2,    5,    2
	};
}

private static final byte _cfgParser_actions[] = init__cfgParser_actions_0();


private static byte[] init__cfgParser_key_offsets_0()
{
	return new byte [] {
	    0,    0,    3,    4,    5,    6,    8,   14,   23,   26,   30,   34,
	   37,   41,   57,   63,   67,   70
	};
}

private static final byte _cfgParser_key_offsets[] = init__cfgParser_key_offsets_0();


private static char[] init__cfgParser_trans_keys_0()
{
	return new char [] {
	    9,   32,  117,  115,  101,  116,    9,   32,    9,   32,   65,   90,
	   97,  122,    9,   32,   95,   48,   57,   65,   90,   97,  122,    9,
	   32,   34,   34,   92,   32,  126,   34,   92,   32,  126,  120,   32,
	  126,   34,   92,   32,  126,   34,   92,   32,   47,   48,   57,   58,
	   64,   65,   70,   71,   96,   97,  102,  103,  126,   48,   57,   65,
	   70,   97,  102,   34,   92,   32,  126,    9,   32,   35,    0
	};
}

private static final char _cfgParser_trans_keys[] = init__cfgParser_trans_keys_0();


private static byte[] init__cfgParser_single_lengths_0()
{
	return new byte [] {
	    0,    3,    1,    1,    1,    2,    2,    3,    3,    2,    2,    1,
	    2,    2,    0,    2,    3,    0
	};
}

private static final byte _cfgParser_single_lengths[] = init__cfgParser_single_lengths_0();


private static byte[] init__cfgParser_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    2,    3,    0,    1,    1,    1,
	    1,    7,    3,    1,    0,    0
	};
}

private static final byte _cfgParser_range_lengths[] = init__cfgParser_range_lengths_0();


private static byte[] init__cfgParser_index_offsets_0()
{
	return new byte [] {
	    0,    0,    4,    6,    8,   10,   13,   18,   25,   29,   33,   37,
	   40,   44,   54,   58,   62,   66
	};
}

private static final byte _cfgParser_index_offsets[] = init__cfgParser_index_offsets_0();


private static byte[] init__cfgParser_indicies_0()
{
	return new byte [] {
	    0,    0,    2,    1,    3,    1,    4,    1,    5,    1,    6,    6,
	    1,    6,    6,    7,    7,    1,    8,    8,    9,    9,    9,    9,
	    1,   10,   10,   11,    1,   13,   14,   12,    1,   16,   17,   15,
	    1,   19,   18,    1,   21,   22,   20,    1,   21,   22,   20,   23,
	   20,   23,   20,   23,   20,    1,   24,   24,   24,    1,   26,   27,
	   25,    1,   13,   13,   28,    1,   28,    0
	};
}

private static final byte _cfgParser_indicies[] = init__cfgParser_indicies_0();


private static byte[] init__cfgParser_trans_targs_0()
{
	return new byte [] {
	    1,    0,    2,    3,    4,    5,    6,    7,    8,    7,    8,    9,
	   10,   16,   11,   10,   16,   11,   12,   13,   10,   16,   11,   14,
	   15,   10,   16,   11,   17
	};
}

private static final byte _cfgParser_trans_targs[] = init__cfgParser_trans_targs_0();


private static byte[] init__cfgParser_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    1,    3,    0,    0,    0,
	    5,    0,    0,    0,    7,    7,    0,    0,   13,    9,    9,    0,
	    0,   16,   11,   11,    0
	};
}

private static final byte _cfgParser_trans_actions[] = init__cfgParser_trans_actions_0();


static final int cfgParser_start = 1;
static final int cfgParser_first_final = 16;
static final int cfgParser_error = 0;

static final int cfgParser_en_main = 1;


// line 48 "CfgParser.rl"
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

            
// line 169 "CfgParser.java"
	{
	cs = cfgParser_start;
	}

// line 85 "CfgParser.rl"
            
// line 176 "CfgParser.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_match: do {
	_keys = _cfgParser_key_offsets[cs];
	_trans = _cfgParser_index_offsets[cs];
	_klen = _cfgParser_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _cfgParser_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _cfgParser_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _cfgParser_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _cfgParser_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _cfgParser_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _cfgParser_indicies[_trans];
	cs = _cfgParser_trans_targs[_trans];

	if ( _cfgParser_trans_actions[_trans] != 0 ) {
		_acts = _cfgParser_trans_actions[_trans];
		_nacts = (int) _cfgParser_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _cfgParser_actions[_acts++] )
			{
	case 0:
// line 4 "CfgParser.rl"
	{
		varNameStart = p;
	}
	break;
	case 1:
// line 8 "CfgParser.rl"
	{
        varName = new String(Arrays.copyOfRange(data, varNameStart, p));
	}
	break;
	case 2:
// line 12 "CfgParser.rl"
	{
		varValueRawStart = p;
	}
	break;
	case 3:
// line 16 "CfgParser.rl"
	{
        varValue += new String(Arrays.copyOfRange(data, varValueRawStart, p));
	}
	break;
	case 4:
// line 20 "CfgParser.rl"
	{
        varValue += (char)data[p - 1];
	}
	break;
	case 5:
// line 24 "CfgParser.rl"
	{
        varValue += (((data[p - 2] & 0x40) != 0 ? 9 : 0) + (data[p - 2] & 0x0F)) << 4 | (((data[p - 1] & 0x40) != 0 ? 9 : 0) + (data[p - 1] & 0x0F));
	}
	break;
// line 292 "CfgParser.java"
			}
		}
	}

case 2:
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
case 5:
	}
	break; }
	}

// line 86 "CfgParser.rl"

            if (cs >= cfgParser_first_final) {
				callback.apply(Context.CFG_PROPERTY, varName, varValue, new String(line), p - bufferStart);
			} else {
				callback.apply(Context.CFG_PROPERTY, empty, empty, new String(line), p - bufferStart);
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