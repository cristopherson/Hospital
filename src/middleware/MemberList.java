package middleware;

import java.util.*;

public class MemberList {

	private LinkedList<String> memberlist;

	/* Format shall be Hospital Name:IP Address: Port */
	public MemberList() {
		this.memberlist = new LinkedList<String>();
	}

	public void addLastMember(String newmember) {
		this.memberlist.addLast(newmember);
	}

	public void removeFirstMember() {
		this.memberlist.removeFirst();
	}

	public String getFirstMember() {
		return (this.memberlist.getFirst());
	}

	public String getLastMember() {
		return (this.memberlist.getLast());
	}

	public int getAmountMembers() {
		return (this.memberlist.size());
	}

	public String getIndexedMember(int index) {
		return (this.memberlist.get(index));
	}

	public String getHospitalName() {
		String[] elements = getLastMember().split(";");
		return (elements[0]);
	}

	public String getIpAddress() {
		String[] elements = getLastMember().split(";");
		return (elements[1]);
	}

	public String getPort() {
		String[] elements = getLastMember().split(";");
		return (elements[2]);
	}

	public String getFlatMemberList() {
		String flattening = "";
		int amount = getAmountMembers();
		int i;

		if (amount > 0) {
			flattening = getIndexedMember(0);
		}

		for (i = 1; i < amount; i++) {
			flattening = flattening + "_#_" + getIndexedMember(i);
		}

		return (flattening);
	}

	public void setFlatMemberList(String flatmemberlist) {
		String[] members = flatmemberlist.split("_#_");
		int length = members.length;
		int i;

		for (i = 0; i < length; i++) {
			addLastMember(members[i]);
		}
	}

	public String inquireNewOwner() {
		if(getAmountMembers() <= 0)
			return "";
		
		removeFirstMember();
		return (getFirstMember());
	}

	public static String createMemberListItem(String id, String hostName,
			String port) {
		return id + ";" + hostName + ";" + port;
	}
}
