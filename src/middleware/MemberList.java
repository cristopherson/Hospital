package middleware;

import java.util.*;

public class MemberList {

	private LinkedList<String> memberlist;

	/* Format shall be Hospital Name:IP Address: Port */
	public MemberList() {
		this.memberlist = new LinkedList<String>();
	}

	public void AddLastMember(String newmember) {
		this.memberlist.addLast(newmember);
	}

	public void RemoveFirstMember() {
		this.memberlist.removeFirst();
	}

	public String GetFirstMember() {
		return (this.memberlist.getFirst());
	}

	public String GetLastMember() {
		return (this.memberlist.getLast());
	}

	public int GetAmountMembers() {
		return (this.memberlist.size());
	}

	public String GetIndexedMember(int index) {
		return (this.memberlist.get(index));
	}

	public String GetHospitalName() {
		String[] elements = GetLastMember().split(";");
		return (elements[0]);
	}

	public String GetIpAddress() {
		String[] elements = GetLastMember().split(";");
		return (elements[1]);
	}

	public String GetPort() {
		String[] elements = GetLastMember().split(";");
		return (elements[2]);
	}

	public String GetFlatMemberList() {
		String flattening = "";
		int amount = GetAmountMembers();
		int i;

		if (amount > 0) {
			flattening = GetIndexedMember(0);
		}

		for (i = 1; i < amount; i++) {
			flattening = flattening + "_#_" + GetIndexedMember(i);
		}

		return (flattening);
	}

	public void SetFlatMemberList(String flatmemberlist) {
		String[] members = flatmemberlist.split("_#_");
		int length = members.length;
		int i;

		for (i = 0; i < length; i++) {
			AddLastMember(members[i]);
		}
	}

	public String InquireNewOwner() {
		if(GetAmountMembers() <= 0)
			return "";
		
		RemoveFirstMember();
		return (GetFirstMember());
	}

	public static String createMemberListItem(String id, String hostName,
			String port) {
		return id + ";" + hostName + ";" + port;
	}
}
