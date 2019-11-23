package com.ludovic.vimont;

public class GeoHash {
	private static final char[] BASE32 = { 
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
			'y', 'z' 
	};
	private long bits = 0;
	private byte significantBits = 0;
	
	final void addOnBitToEnd() {
		significantBits++;
		bits <<= 1;
		bits = bits | 0x1;
	}

	final void addOffBitToEnd() {
		significantBits++;
		bits <<= 1;
	}

	public long getBits() {
		return bits;
	}

	public void leftShift(long bits) {
		this.bits <<= bits;
	}

	public byte getSignificantBits() {
		return significantBits;
	}

	public String toBase32() {
		if (significantBits % 5 != 0) {
			throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
		}
		StringBuilder buf = new StringBuilder();
		long firstFiveBitsMask = 0xf800000000000000l;
		int numberOfCharacters = (int) Math.ceil(((double) significantBits / 5));
		for (int i = 0; i < numberOfCharacters; i++) {
			int pointer = (int) ((bits & firstFiveBitsMask) >>> 59);
			buf.append(BASE32[pointer]);
			bits <<= 5;
		}
		return buf.toString();
	}

	@Override
	public String toString() {
		return "GeoHash [bits=" + bits + " (" + Long.toString(bits, 2) + "), significantBits=" + significantBits + "]";
	}
}