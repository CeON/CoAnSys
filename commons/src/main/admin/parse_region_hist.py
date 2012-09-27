import sys

histFileName = sys.argv[1]
file = open(histFileName,"rb")
line = "nonempty_line"
serverTableRegions = {}
serverTableSizeMB = {}
tableSizeMB = {}
while (line != ""):
	line = file.readline()
	if (line != ""): 
		parts = line.split('\t')
		server = parts[0]
		region = parts[1]
		table = region.split(',')[0]
		storefilesSizeMB = int(parts[2])

		serverTable = table  + " " + server
		regionCnt = serverTableRegions.get(serverTable, 0)
		serverTableRegions[serverTable] = regionCnt + 1
		currServerTableSizeMB = serverTableSizeMB.get(serverTable, 0)
		serverTableSizeMB[serverTable] = currServerTableSizeMB + storefilesSizeMB
		currTableSizeMB = tableSizeMB.get(table, [])
		currTableSizeMB.append(storefilesSizeMB)	
		tableSizeMB[table] = currTableSizeMB
file.close()

print "---------------------------------"
print "server - table - regionCnt"
print "---------------------------------"
for key, value in serverTableRegions.items():
	keyParts = key.split(' ')
	print keyParts[0], keyParts[1], str(value)

print "---------------------------------"
print "server - table - storefilesSizeMB"
print "---------------------------------"
for key, value in serverTableSizeMB.items():
	keyParts = key.split(' ')
	print keyParts[0], keyParts[1], str(value)

print "---------------------------------"
print "table - list of storefilesSizeMB"
print "---------------------------------"
for key, value in tableSizeMB.items():
	value.sort()
	print key, value
