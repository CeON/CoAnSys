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

package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.application.ScoobiApp

/**
 * ScoobiApp variant that doesn't add to Distributed Cache jars that are already there.
 * It became obsolete with Scoobi 0.7.3.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class MyScoobiApp extends ScoobiApp {}
