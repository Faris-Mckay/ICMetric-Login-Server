package com.peerchat.kent.security.secret;

/**
 * This package handles all function of the shamir secret sharing implementation
 *
 * @author Faris McKay
 *
 */

public class package_info {
    /**
     * Credits: <https://github.com/codahale/jsecretsharing> for providing the Open Source implementation for this
     *
     * Usage: For example see below before implementation
     *
     * ShareBuilder builder = new ShareBuilder("I am Batman. Seriously.".getBytes(), 2, 512);
     * List shares = builder.build(10);

     // takes 2 shares, recovers secret
     List someShares = new ArrayList();
     someShares.add(shares.get(2));
     someShares.add(shares.get(7));
     ShareCombiner combiner = new ShareCombiner(someShares);
     System.err.println(new String(combiner.combine()));

     // omg I'm batman
     */
}
