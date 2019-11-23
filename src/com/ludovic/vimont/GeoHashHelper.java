package com.ludovic.vimont;

public class GeoHashHelper {
	private static final int MAX_BIT_PRECISION = 64;
	private static final int MAX_GEOHASH_CHARACTER_PRECISION = 12;

	public static String getGeohash(Location location) {
		return getGeohash(location, MAX_GEOHASH_CHARACTER_PRECISION);
	}

	public static String getGeohash(Location location, int numberOfCharacters) {
		int desiredPrecision = getDesiredPrecsion(numberOfCharacters);
		GeoHash geohash = new GeoHash();

		boolean isEvenBit = true;
		double[] latitudeRange = { -90, 90 };
		double[] longitudeRange = { -180, 180 };

		while (geohash.getSignificantBits() < desiredPrecision) {
			if (isEvenBit) {
				divideRangeEncode(geohash, location.lng(), longitudeRange);
			} else {
				divideRangeEncode(geohash, location.lat(), latitudeRange);
			}
			isEvenBit = !isEvenBit;
		}

		geohash.leftShift(MAX_BIT_PRECISION - desiredPrecision);
		
		System.out.println(geohash);
		return geohash.toBase32();
	}

	private static int getDesiredPrecsion(int numberOfCharacters) {
		if (numberOfCharacters > MAX_GEOHASH_CHARACTER_PRECISION) {
			throw new IllegalArgumentException("A geohash can only be " + MAX_GEOHASH_CHARACTER_PRECISION + " character long.");
		}
		int desiredPrecision = 60;
		if (numberOfCharacters * 5 <= 60) {
			desiredPrecision = numberOfCharacters * 5;
		}
		return desiredPrecision = Math.min(desiredPrecision, MAX_BIT_PRECISION);
	}

	private static void divideRangeEncode(GeoHash geohash, double value, double[] range) {
		double mid = (range[0] + range[1]) / 2;
		if (value >= mid) {
			geohash.addOnBitToEnd();
			range[0] = mid;
		} else {
			geohash.addOffBitToEnd();
			range[1] = mid;
		}
	}
}