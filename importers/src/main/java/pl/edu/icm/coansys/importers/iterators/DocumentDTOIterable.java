/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import pl.edu.icm.coansys.importers.models.DocumentDTO;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public abstract class DocumentDTOIterable implements Iterable<DocumentDTO> {

    /*
     * Path to input data and collection name
     */
    private String dataPath;
    private String collection;
    /*
     * Item which will be returned by next() call; it has to be set in
     * subclasses by prepareNextItem()
     */
    private DocumentDTO nextItem;
    private boolean nextItemPrepared = false;

    public DocumentDTOIterable(String dataPath, String collection) {
        this.dataPath = dataPath;
        this.collection = collection;
    }

    /**
     * Prepare next value for the iterator
     *
     * @param dataPath path with input data
     * @return value prepared for the iterator
     */
    protected abstract DocumentDTO prepareNextItem(String dataPath, String collection);

    @Override
    public Iterator<DocumentDTO> iterator() {
        return new Iterator<DocumentDTO>() {

            @Override
            public boolean hasNext() {
                if (!nextItemPrepared) {
                    nextItem = prepareNextItem(dataPath, collection);
                    nextItemPrepared = true;
                }
                return nextItem != null;
            }

            @Override
            public DocumentDTO next() {
                if (!nextItemPrepared) {
                    nextItem = prepareNextItem(dataPath, collection);
                    nextItemPrepared = true;
                }
                if (hasNext()) {
                    nextItemPrepared = false;
                    return nextItem;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Operation remove() is not supported");
            }
        };
    }
}
