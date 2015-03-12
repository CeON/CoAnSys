/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.deduplication.document.voter;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 */
public class Vote {

    private VoteStatus status;
    private Float probability; 

    public Vote(VoteStatus status) {
        if (status.equals(VoteStatus.PROBABILITY)) {
            throw new IllegalArgumentException("probability not provided");
        }
        this.status = status;
    }
    
    public Vote(VoteStatus status, float probability) {
        this.status = status;
        if (status.equals(VoteStatus.PROBABILITY)) {
            this.probability = probability;
        }
    }

    public enum VoteStatus {

        EQUALS,
        NOT_EQUALS,
        ABSTAIN,
        PROBABILITY
    }

    public VoteStatus getStatus() {
        return status;
    }

    public Float getProbability() {
        return probability;
    }
}
