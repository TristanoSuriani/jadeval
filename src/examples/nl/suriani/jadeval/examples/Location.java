package nl.suriani.jadeval.examples;

class Location {
	private String name;
	private String zipCode;
	private String municipality;
	private long inhabitants;

	public Location(String name, String zipCode, String municipality, long inhabitants) {
		this.name = name;
		this.zipCode = zipCode;
		this.municipality = municipality;
		this.inhabitants = inhabitants;
	}

	public String getName() {
		return name;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getMunicipality() {
		return municipality;
	}

	public long getInhabitants() {
		return inhabitants;
	}
}
