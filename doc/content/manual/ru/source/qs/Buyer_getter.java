public String getFullName() {
    return (surName!=null?surName:"")+" "+(firstName!=null?firstName:"");

	}
	
public void setFullName(String fullName) {
    this.fullName = fullName;
}