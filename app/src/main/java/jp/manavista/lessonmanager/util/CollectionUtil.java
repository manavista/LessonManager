/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Collection Utility
 *
 * <p>
 * Overview:<br>
 * Classes that summarize operations related to collections.
 * Operations on Set only, for now.
 * </p>
 *
 */
public final class CollectionUtil {

    /**
     *
     * Inequality
     *
     * <p>
     * Overview:<br>
     * Enumerated elements of inequality symbols used for judgment.
     * </p>
     */
    private enum Inequality {
        RatherThan,
        RatherThanEqual,
        LessThan,
        LessThanEqual,
    }

    /**
     *
     * Calculate
     *
     * <p>
     * Overview:<br>
     * Enumerated elements of Calculate symbols used for judgment.
     * </p>
     */
    private enum Calculate {
        Max,
        Min
    }

    /**
     *
     * Contain a value smaller than the value
     *
     * <p>
     * Overview:<br>
     * Determination of a value smaller than the target value.
     * </p>
     *
     * @param set target value set
     * @param target target value
     * @return contains result
     */
    public static boolean containsLessThan(Set<Integer> set, int target) {

        for ( int val : set ) {
            if( val < target ) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Get a min value
     *
     * <p>
     * Overview:<br>
     * Get the minimum value in a set above a certain value.
     * </p>
     *
     * @param set target value set
     * @param target target value
     * @return minimum value
     */
    public static int minRatherThanEqual(Set<Integer> set, int target) {
        return calculate(set, target, Inequality.RatherThanEqual, Calculate.Min);
    }

    /**
     *
     * Get a maximum value
     *
     * <p>
     * Overview:<br>
     * Get the max value in a set above a certain value.
     * </p>
     *
     * @param set target value set
     * @param target target value
     * @return maximum value
     */
    public static int maxLessThan(Set<Integer> set, int target) {
        return calculate(set, target, Inequality.LessThan, Calculate.Max);
    }

    /**
     *
     * Get a maximum value
     *
     * <p>
     * Overview:<br>
     * Get the max value in a set above a certain value.
     * </p>
     *
     * @param set target value set
     * @param target target value
     * @return maximum value
     */
    public static int maxLessThanEqual(Set<Integer> set, int target) {
        return calculate(set, target, Inequality.LessThanEqual, Calculate.Max);
    }

    /**
     *
     * Calculate
     *
     * <p>
     * Overview:<br>
     * Based on the inequality sign and the calculation type specified in the argument,
     * the value is calculated from the set.
     * </p>
     *
     * @param set target value set
     * @param targetValue target value
     * @param inequality inequality symbols used for judgment
     * @param calculate calculate symbols used for judgment
     * @return calculated value
     */
    private static int calculate(Set<Integer> set, int targetValue, Inequality inequality, Calculate calculate) {

        Set<Integer> target = new HashSet<>();

        for( int val : set ) {

            boolean add = false;

            switch (inequality) {

                case RatherThan:
                    add = val > targetValue;
                    break;
                case RatherThanEqual:
                    add =  val >= targetValue;
                    break;
                case LessThan:
                    add = val < targetValue;
                    break;
                case LessThanEqual:
                    add = val <= targetValue;
                    break;
            }

            if( add ) {
                target.add(val);
            }
        }

        if( Calculate.Max.equals(calculate) ){
            return Collections.max(target);
        } else if( Calculate.Min.equals(calculate) ) {
            return Collections.min(target);
        } else {
            throw new RuntimeException("not exist Calculate value!");
        }
    }
}
