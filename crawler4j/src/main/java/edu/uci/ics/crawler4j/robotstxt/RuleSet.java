package edu.uci.ics.crawler4j.robotstxt;

import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class RuleSet.
 */
public class RuleSet extends TreeSet<String>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(String str)
	{
		SortedSet<String> sub = headSet(str);
		if (!sub.isEmpty() && str.startsWith((String) sub.last()))
		{
			// no need to add; prefix is already present
			return false;
		}
		boolean retVal = super.add(str);
		sub = tailSet(str + "\0");
		while (!sub.isEmpty() && ((String) sub.first()).startsWith(str))
		{
			// remove redundant entries
			sub.remove(sub.first());
		}
		return retVal;
	}

	/**
	 * Contains prefix of.
	 * 
	 * @param s
	 *            the s
	 * @return true, if successful
	 */
	public boolean containsPrefixOf(String s)
	{
		SortedSet<String> sub = headSet(s);
		// because redundant prefixes have been eliminated,
		// only a test against last item in headSet is necessary
		if (!sub.isEmpty() && s.startsWith((String) sub.last()))
		{
			return true; // prefix substring exists
		}
		// might still exist exactly (headSet does not contain boundary)
		return contains(s);
	}
}
