package de.depascaldc.management.util;

public enum UTF8Chars {
	double_angle_quotation_mark_right('»'), double_angle_quotation_mark_left('«'),

	registered_sign('®'), copyright_sign('©'), trademark_sign('™'),

	cent_sign('¢'), currency_sign('¤'),

	degree_sign('°'), division_sign('÷'), euro_sign('€'),

	bullet('•'), MODIFIER_LETTER_CAPITAL_I('ᴵ'), MODIFIER_LETTER_SMALL_I('ᶦ'),

	superscript_0('⁰'), superscript_1('¹'), superscript_2('²'), superscript_3('³'), superscript_4('⁴'),
	superscript_5('⁵'), superscript_6('⁶'), superscript_7('⁷'), superscript_8('⁸'), superscript_9('⁹'),
	superscript_minus('⁻'), superscript_plus('⁺'), superscript_equal('⁼'),

	subscript_minus('₋'), subscript_plus('₊'), subscript_equal('₌'), subscript_bracket_open('₍'),
	subscript_bracket_close('₎'), subscript_x('ₓ'), subscript_0('₀'), subscript_1('₁'), subscript_2('₂'),
	subscript_3('₃'), subscript_4('₄'), subscript_5('₅'), subscript_6('₆'), subscript_7('₇'), subscript_8('₈'),
	subscript_9('₉');

	private char character;

	UTF8Chars(char character) {
		this.character = character;
	}

	public char get() {
		return character;
	}
}
