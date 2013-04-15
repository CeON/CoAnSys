/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.logs;

import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.edu.icm.coansys.importers.models.LogsProtos;
import pl.edu.icm.coansys.importers.models.LogsProtos.LogsMessage;
import pl.edu.icm.synat.api.services.SynatServiceRef;
import pl.edu.icm.synat.api.services.audit.AuditService;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;
import pl.edu.icm.synat.api.services.audit.model.AuditQueryConditions;
import pl.edu.icm.synat.common.ListingResult;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditServiceToRemoteHTable {

    @SynatServiceRef(serviceId = "AuditService")
    private AuditService auditService;

    private AuditServiceToRemoteHTable() {
    }

    public static void main(final String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("Usage: AcquireAuditService <rest_server> <port> <table_name> <column_family> <column>");
            System.exit(1);
        }

        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("audit-client.xml");
        final AuditServiceToRemoteHTable client = applicationContext.getBean("auditServiceClient2", AuditServiceToRemoteHTable.class);

        client.doAcquire(args[0], Integer.parseInt(args[1]), args[2], args[3], args[4]);

        applicationContext.close();
    }

    private void doAcquire(String restServer, int port, String tableName, String columnFamily, String column) throws IOException {
        RemoteHTable htable = new RemoteHTable(new Client(new Cluster().add(restServer, port)), tableName);

        AuditQueryConditions condition = new AuditQueryConditions();
        int limit = 100;

        ListingResult<AuditEntry> result = auditService.queryAudit(condition, limit);
        while (!result.isEmpty()) {
            List<AuditEntry> items = result.getItems();
            for (AuditEntry item : items) {
                Put put = new Put(Bytes.toBytes(item.getEventId()));
                byte[] family = Bytes.toBytes(columnFamily);
                byte[] qualifier = Bytes.toBytes(column);

                LogsMessage serialize = AuditEntry2Protos.serialize(item);
                if (!serialize.getEventType().equals(LogsProtos.EventType.CUSTOM)) {
                    byte[] toByteArray = serialize.toByteArray();
                    put.add(family, qualifier, toByteArray);
                    htable.put(put);
                }
            }

            String nextToken = result.getNextToken();
            result = auditService.queryAudit(condition, nextToken, limit);
        }
    }
}