
// line 1 "SiiTextParser.rl"

// line 98 "SiiTextParser.rl"


package pt.marmelo.ets2sync.parser;

import java.util.*;

public class SiiTextParser {
    
// line 14 "SiiTextParser.java"
private static byte[] init__siiParserText_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    3,    1,    4,    1,
	    5,    1,    6,    1,    7,    1,    8,    1,    9,    1,   12,    1,
	   13,    1,   14,    1,   15,    1,   16,    2,    4,    6,    2,    5,
	    0,    2,    8,   10,    2,   11,    9,    2,   14,   12,    2,   15,
	   12
	};
}

private static final byte _siiParserText_actions[] = init__siiParserText_actions_0();


private static short[] init__siiParserText_key_offsets_0()
{
	return new short [] {
	    0,    0,    1,    2,    3,    4,    5,    6,    7,    8,   11,   15,
	   25,   34,   37,   39,   48,   58,   61,   72,   83,   92,   94,   99,
	  106,  112,  116,  120,  124,  127,  131,  137,  143,  147,  149,  152,
	  153,  164,  175,  182,  185
	};
}

private static final short _siiParserText_key_offsets[] = init__siiParserText_key_offsets_0();


private static char[] init__siiParserText_trans_keys_0()
{
	return new char [] {
	   83,  105,  105,   78,  117,  110,  105,  116,   32,    9,   13,   32,
	  123,    9,   13,   32,   95,    9,   13,   48,   57,   65,   90,   97,
	  122,    9,   32,   95,   48,   57,   65,   90,   97,  122,    9,   32,
	   58,    9,   32,    9,   32,   95,   48,   57,   65,   90,   97,  122,
	    9,   32,   46,   95,   48,   57,   65,   90,   97,  122,    9,   32,
	  123,   32,   95,  125,    9,   13,   48,   57,   65,   90,   97,  122,
	   32,   95,  125,    9,   13,   48,   57,   65,   90,   97,  122,   58,
	   91,   95,   48,   57,   65,   90,   97,  122,    9,   32,    9,   32,
	   34,   33,  126,    9,   10,   13,   32,   34,   33,  126,   10,   13,
	   32,   33,   35,  126,   34,   92,   32,  126,   34,   92,   32,  126,
	    9,   10,   13,   32,   34,   92,  120,   34,   92,   32,  126,   48,
	   57,   65,   70,   97,  102,   48,   57,   65,   70,   97,  102,   34,
	   92,   32,  126,   48,   57,   93,   48,   57,   58,   32,   95,  125,
	    9,   13,   48,   57,   65,   90,   97,  122,   32,   95,  125,    9,
	   13,   48,   57,   65,   90,   97,  122,   95,   48,   57,   65,   90,
	   97,  122,   32,    9,   13,   32,    9,   13,    0
	};
}

private static final char _siiParserText_trans_keys[] = init__siiParserText_trans_keys_0();


private static byte[] init__siiParserText_single_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,    2,
	    3,    3,    2,    3,    4,    3,    3,    3,    3,    2,    3,    5,
	    2,    2,    2,    4,    3,    2,    0,    0,    2,    0,    1,    1,
	    3,    3,    1,    1,    1
	};
}

private static final byte _siiParserText_single_lengths[] = init__siiParserText_single_lengths_0();


private static byte[] init__siiParserText_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,    1,    4,
	    3,    0,    0,    3,    3,    0,    4,    4,    3,    0,    1,    1,
	    2,    1,    1,    0,    0,    1,    3,    3,    1,    1,    1,    0,
	    4,    4,    3,    1,    1
	};
}

private static final byte _siiParserText_range_lengths[] = init__siiParserText_range_lengths_0();


private static short[] init__siiParserText_index_offsets_0()
{
	return new short [] {
	    0,    0,    2,    4,    6,    8,   10,   12,   14,   16,   19,   23,
	   30,   37,   41,   44,   51,   59,   63,   71,   79,   86,   89,   94,
	  101,  106,  110,  114,  119,  123,  127,  131,  135,  139,  141,  144,
	  146,  154,  162,  167,  170
	};
}

private static final short _siiParserText_index_offsets[] = init__siiParserText_index_offsets_0();


private static byte[] init__siiParserText_indicies_0()
{
	return new byte [] {
	    0,    1,    2,    1,    3,    1,    4,    1,    5,    1,    6,    1,
	    7,    1,    8,    1,    9,    9,    1,    9,   10,    9,    1,   10,
	   11,   10,   11,   11,   11,    1,   12,   12,   13,   13,   13,   13,
	    1,   14,   14,   15,    1,   16,   16,    1,   16,   16,   17,   17,
	   17,   17,    1,   18,   18,   19,   20,   20,   20,   20,    1,   21,
	   21,   22,    1,   23,   24,   25,   23,   24,   24,   24,    1,   26,
	   27,   28,   26,   27,   27,   27,    1,   30,   31,   29,   29,   29,
	   29,    1,   32,   32,    1,   32,   33,   35,   34,    1,   32,   36,
	   36,   33,   35,   34,    1,   36,   36,   37,   37,    1,   39,   40,
	   38,    1,   42,   43,   41,    1,   39,   44,   44,   39,    1,   45,
	   45,   46,    1,   48,   49,   47,    1,   50,   50,   50,    1,   51,
	   51,   51,    1,   53,   54,   52,    1,   55,    1,   56,   55,    1,
	   30,    1,   57,   58,   59,   57,   58,   58,   58,    1,   60,   11,
	   61,   60,   11,   11,   11,    1,   20,   20,   20,   20,    1,   62,
	   62,    1,   63,   63,    1,    0
	};
}

private static final byte _siiParserText_indicies[] = init__siiParserText_indicies_0();


private static byte[] init__siiParserText_trans_targs_0()
{
	return new byte [] {
	    2,    0,    3,    4,    5,    6,    7,    8,    9,   10,   11,   12,
	   13,   12,   13,   14,   15,   16,   17,   38,   16,   17,   18,   19,
	   20,   36,   19,   20,   36,   20,   21,   33,   22,   23,   24,   25,
	   19,   24,   26,   27,   28,   26,   27,   28,   19,   29,   30,   26,
	   27,   28,   31,   32,   26,   27,   28,   34,   35,   37,   12,   39,
	   37,   39,   40,   40
	};
}

private static final byte _siiParserText_trans_targs[] = init__siiParserText_trans_targs_0();


private static byte[] init__siiParserText_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
	    3,    0,    0,    0,    0,    5,    7,    0,    0,    0,    0,    9,
	   31,    9,    0,   13,    0,    0,   15,    0,    0,   37,   37,   17,
	   40,    0,   21,    0,    0,    0,   23,   23,   19,    0,    0,   43,
	   25,   25,    0,    0,   46,   27,   27,    0,    0,   11,   34,   11,
	    0,    0,   29,    0
	};
}

private static final byte _siiParserText_trans_actions[] = init__siiParserText_trans_actions_0();


private static byte[] init__siiParserText_eof_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,   29,    0
	};
}

private static final byte _siiParserText_eof_actions[] = init__siiParserText_eof_actions_0();


static final int siiParserText_start = 1;
static final int siiParserText_first_final = 39;
static final int siiParserText_error = 0;

static final int siiParserText_en_main = 1;


// line 106 "SiiTextParser.rl"
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
    
        
// line 196 "SiiTextParser.java"
	{
	cs = siiParserText_start;
	}

// line 121 "SiiTextParser.rl"

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

        
// line 222 "SiiTextParser.java"
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
	_keys = _siiParserText_key_offsets[cs];
	_trans = _siiParserText_index_offsets[cs];
	_klen = _siiParserText_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _siiParserText_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _siiParserText_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _siiParserText_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _siiParserText_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _siiParserText_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _siiParserText_indicies[_trans];
	cs = _siiParserText_trans_targs[_trans];

	if ( _siiParserText_trans_actions[_trans] != 0 ) {
		_acts = _siiParserText_trans_actions[_trans];
		_nacts = (int) _siiParserText_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _siiParserText_actions[_acts++] )
			{
	case 0:
// line 4 "SiiTextParser.rl"
	{
		unitClassStart = p;
	}
	break;
	case 1:
// line 8 "SiiTextParser.rl"
	{
		unitClass = new String(Arrays.copyOfRange(data, unitClassStart, p));
	}
	break;
	case 2:
// line 12 "SiiTextParser.rl"
	{
		unitInstanceStart = p;
	}
	break;
	case 3:
// line 16 "SiiTextParser.rl"
	{
        unitInstance = new String(Arrays.copyOfRange(data, unitInstanceStart, p));
	}
	break;
	case 4:
// line 20 "SiiTextParser.rl"
	{
        callback.apply(Context.UNIT_START, unitClass, unitInstance, attrSourceValue, p - start);
	}
	break;
	case 5:
// line 24 "SiiTextParser.rl"
	{
        callback.apply(Context.UNIT_END, unitClass, unitInstance, attrSourceValue, p - start);
	}
	break;
	case 6:
// line 28 "SiiTextParser.rl"
	{
		attrNameStart = p;
	}
	break;
	case 7:
// line 32 "SiiTextParser.rl"
	{
        attrName = new String(Arrays.copyOfRange(data, attrNameStart, p));
	}
	break;
	case 8:
// line 36 "SiiTextParser.rl"
	{
		attrRawValue = "";
		attrSourceValueStart = p;
	}
	break;
	case 9:
// line 41 "SiiTextParser.rl"
	{
        attrSourceValue = new String(Arrays.copyOfRange(data, attrSourceValueStart, p));
        callback.apply(Context.ATTRIBUTE, attrName, attrRawValue, attrSourceValue, p - start);
	}
	break;
	case 10:
// line 46 "SiiTextParser.rl"
	{
		attrBareValueStart = p;
	}
	break;
	case 11:
// line 50 "SiiTextParser.rl"
	{
        attrRawValue = new String(Arrays.copyOfRange(data, attrBareValueStart, p));
	}
	break;
	case 12:
// line 54 "SiiTextParser.rl"
	{
		attrQuotedValueRawStart = p;
	}
	break;
	case 13:
// line 58 "SiiTextParser.rl"
	{
        attrRawValue += new String(Arrays.copyOfRange(data, attrQuotedValueRawStart, p));
	}
	break;
	case 14:
// line 62 "SiiTextParser.rl"
	{
        attrRawValue += (char)data[p - 1];
	}
	break;
	case 15:
// line 66 "SiiTextParser.rl"
	{
        attrRawValue += (((data[p - 2] & 0x40) != 0 ? 9 : 0) + (data[p - 2] & 0x0F)) << 4 | (((data[p - 1] & 0x40) != 0 ? 9 : 0) + (data[p - 1] & 0x0F));
	}
	break;
	case 16:
// line 70 "SiiTextParser.rl"
	{
		success = true;
	}
	break;
// line 406 "SiiTextParser.java"
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
	if ( p == eof )
	{
	int __acts = _siiParserText_eof_actions[cs];
	int __nacts = (int) _siiParserText_actions[__acts++];
	while ( __nacts-- > 0 ) {
		switch ( _siiParserText_actions[__acts++] ) {
	case 16:
// line 70 "SiiTextParser.rl"
	{
		success = true;
	}
	break;
// line 433 "SiiTextParser.java"
		}
	}
	}

case 5:
	}
	break; }
	}

// line 141 "SiiTextParser.rl"

        return success;
    }
}