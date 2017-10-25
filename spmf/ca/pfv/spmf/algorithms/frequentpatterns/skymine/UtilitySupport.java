package ca.pfv.spmf.algorithms.frequentpatterns.skymine;

/**
 * This represents a pair of support and utility values, used by the Skymine
 * algorithm.
 * 
 * Copyright (c) 2015 Vikram Goyal, Ashish Sureka, Dhaval Patel, Siddharth Dawar
 * 
 * This file is part of the SPMF DATA MINING SOFTWARE *
 * (http://www.philippe-fournier-viger.com/spmf).
 * 
 * SPMF is free software: you can redistribute it and/or modify it under the *
 * terms of the GNU General Public License as published by the Free Software *
 * Foundation, either version 3 of the License, or (at your option) any later *
 * version. SPMF is distributed in the hope that it will be useful, but WITHOUT
 * ANY * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @see AlgoSkyMine
 * @see UPTree
 * 
 * @author Vikram Goyal, Ashish Sureka, Dhaval Patel, Siddharth Dawar
 */

public class UtilitySupport {
	
	/** a support value */
	public int support = 0;
	
	/** a utility value */
	public long utility = 0;

	/**
	 * Constructor
	 * @param support a support value
	 * @param utility a utility value
	 */
	public UtilitySupport(int support, long utility) {
		this.support = support;
		this.utility = utility;
	}
}