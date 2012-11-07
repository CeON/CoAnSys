/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.logsacquisition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos.LogMessage;
import pl.edu.icm.coansys.logsanalysis.transformers.AuditEntry2Protos;
import pl.edu.icm.coansys.logsanalysis.transformers.BytesArray2SequenceFile;
import pl.edu.icm.synat.api.services.SynatServiceRef;
import pl.edu.icm.synat.api.services.audit.AuditService;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;
import pl.edu.icm.synat.api.services.audit.model.AuditQueryConditions;
import pl.edu.icm.synat.common.ListingResult;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditServiceToSequenceFile {

    @SynatServiceRef(serviceId = "AuditService")
    private AuditService auditService;

    private AuditServiceToSequenceFile() {}
    
    public static void main(final String[] args) throws IOException {
        
        if (args.length < 1) {
            System.err.println("Usage: AcquireAuditService <output_dir>");
            System.exit(1);
        }

        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("audit-client.xml");
        final AuditServiceToSequenceFile client = applicationContext.getBean("auditServiceClient1", AuditServiceToSequenceFile.class);

        client.doAcquire(args[0]);

        applicationContext.close();
    }

    private void doAcquire(String directoryPath) throws IOException {
        AuditQueryConditions condition = new AuditQueryConditions();
        int limit = 100;

        ListingResult<AuditEntry> result = auditService.queryAudit(condition, limit);
        while (!result.isEmpty()) {
            List<byte[]> serializedLogs = new ArrayList<byte[]>();
            List<AuditEntry> items = result.getItems();
            for (AuditEntry item : items) {
                LogMessage serialize = AuditEntry2Protos.serialize(item);
                byte[] toByteArray = serialize.toByteArray();
                serializedLogs.add(toByteArray);
            }
            
            String filePath = directoryPath + "/" + "logs_sequence_file_" + System.currentTimeMillis() + ".log";
            BytesArray2SequenceFile.write(serializedLogs, filePath);
            
            String nextToken = result.getNextToken();
            result = auditService.queryAudit(condition, nextToken, limit);
        }
    }
}