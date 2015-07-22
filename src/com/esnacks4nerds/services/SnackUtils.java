package com.esnacks4nerds.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.builder.CompareToBuilder;

import com.esnacks4nerds.model.entity.SnackVote;
import com.esnacks4nerds.model.entity.Snacks;
import com.esnacks4nerds.model.entity.VotedSnacks;

public class SnackUtils {

	public static List<Snacks> sortedSnacksOrderByNotOptional(
			List<Snacks> snacksList) {
		Collections.sort(snacksList, new Comparator<Snacks>() {
			
			@Override
			public int compare(Snacks o1, Snacks o2) {
				// TODO Auto-generated method stub
				return new CompareToBuilder().append(o1.isOptional(),
						o2.isOptional()).toComparison();
			}

		});
		return snacksList;
	}


	public static int count(List<SnackVote> list, final int snackid) {
		System.out.println(" No. of entries in votes table: " + list.size() + " " + snackid);
		
		int count = CollectionUtils.countMatches(list, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				// TODO Auto-generated method stub
				return ((SnackVote) object).getSnack_id() == snackid && DateTimeUtils.isValidDate(((SnackVote) object).getDate_voted());
			}

		});
		return count;
	}

	public static List<Snacks> sortByCounts(List<Snacks> list,
			List<SnackVote> snackVotes) {
		List<Snacks> sortedList = new ArrayList<Snacks>();
		List<VotedSnacks> unSortedList = new ArrayList<VotedSnacks>();
		for (Snacks s : list) {
			if (s.isOptional()) {
				VotedSnacks v = new VotedSnacks();
				v.setSnacks(s);
				v.setnVotes(count(snackVotes, s.getId()));
				unSortedList.add(v);
			}
		}

		Collections.sort(unSortedList, new Comparator<VotedSnacks>() {

			@Override
			public int compare(VotedSnacks o1, VotedSnacks o2) {
				// TODO Auto-generated method stub
				return new CompareToBuilder().append(o2.getnVotes(),
						o1.getnVotes()).toComparison();
			}

		});
		
		for ( VotedSnacks vts: unSortedList){
			sortedList.add(vts.getSnacks());
		}
		return sortedList;
	}

	public static boolean contains(String snackName, List<Snacks> snackList){
		boolean res=false;
		for ( Snacks snack: snackList){
			if ( snack.getName().trim().equalsIgnoreCase(snackName.trim())){
				res=true;
				break;
			}
			
		}
		return res;
	}
	
	public static void main(String[] args) {

		List<Snacks> l = new ArrayList<Snacks>();
		Snacks s = new Snacks();
		s.setId(1);
		s.setName("abc");
		s.setOptional(true);
		l.add(s);
		s = new Snacks();
		s.setId(2);
		s.setName("xyz");
		s.setOptional(false);
		l.add(s);
		s = new Snacks();
		s.setId(3);
		s.setName("xyz2");
		s.setOptional(true);
		l.add(s);

		List<Snacks> sortedList = sortedSnacksOrderByNotOptional(l);

		for (Snacks s1 : sortedList) {
			System.out.println(s1.getName() + "  " + s1.isOptional());
		}

		List<SnackVote> l2 = new ArrayList<SnackVote>();
		SnackVote sv = new SnackVote();

		sv.setID(1);
		sv.setNum_votes(1);
		sv.setSnack_id(1);
		l2.add(sv);
		sv = new SnackVote();

		sv.setID(2);
		sv.setNum_votes(1);
		sv.setSnack_id(1);

		l2.add(sv);
		sv = new SnackVote();

		sv.setID(3);
		sv.setNum_votes(1);
		sv.setSnack_id(3);

		l2.add(sv);

		System.out.println(count(l2, 1));

		s = new Snacks();
		s.setId(1);
		s.setName("abc");
		s.setOptional(true);

		s = new Snacks();
		s.setId(2);
		s.setName("abc1");
		s.setOptional(true);

		s = new Snacks();
		s.setId(3);
		s.setName("abc2");
		s.setOptional(true);

		List<Snacks> vlist = sortByCounts(l, l2);

		for (Snacks vts : vlist) {
			System.out.println(vts.getName() + " "
					);
		}

	}

}
