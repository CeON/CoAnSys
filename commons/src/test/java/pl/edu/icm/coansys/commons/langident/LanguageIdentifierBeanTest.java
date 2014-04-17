/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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
package pl.edu.icm.coansys.commons.langident;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class LanguageIdentifierBeanTest {

    private static Map<String, String> examples = new HashMap<String, String>();

    static {
        examples.put("Jak zaśnieżały kłos, zarażający Zdrowego brata. Maszli, pani, oczy, Żeś mogła rzucić górne pastwisko Dla paszy na tym bagnie?", "pl");
        examples.put("The body is with the king, but the king is not with the body. The king is a thing", "en");
        examples.put("Qui, en effet, voudrait supporter les flagellations et les dédains du monde, l’injure de l’oppresseur, l’humiliation de la pauvreté", "fr");
        examples.put("I popoli non dovrebbero avere paura dei propri governi, sono i governi che dovrebbero aver paura dei popoli.", "it");
        examples.put("Denn von den Extremen ist das eine mehr, das andere weniger fehlerhaft.", "de");
    }

    @Test(groups = {"fast"})
    public void langIdent() throws IOException {
        LanguageIdentifierBean langId = new LanguageIdentifierBean();
        for (Map.Entry<String, String> langExample : examples.entrySet()) {
            assertEquals(langId.classify(langExample.getKey()), langExample.getValue());
        }
    }
}
