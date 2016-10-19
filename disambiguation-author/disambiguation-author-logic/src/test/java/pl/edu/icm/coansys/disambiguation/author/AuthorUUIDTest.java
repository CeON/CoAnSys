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

package pl.edu.icm.coansys.disambiguation.author;


import java.util.UUID;


import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import static org.testng.Assert.*;



public class AuthorUUIDTest {
	 
    // Follow name scheme of tests: package_class[_method]
	// TODO: split tests into correct packages, rename
	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void authorTest() {
		Author.Builder ab=DocumentProtos.Author.newBuilder();
        ab.setKey("ala#1");
        ab.setDocId("ala");
        ab.setSurname("a");
        ab.setForenames("b");
        Author au1=ab.build();
        String uuid1=UUID.nameUUIDFromBytes(au1.toByteArray()).toString();
        String uuid2=UUID.nameUUIDFromBytes(au1.toByteArray()).toString();
        assertEquals(uuid1, uuid2);
      
        
        
   	}
   	
 
}

