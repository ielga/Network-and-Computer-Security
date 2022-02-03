package databaseServer;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import sirs.remoteDocs.DatabaseBackupServiceGrpc;
import sirs.remoteDocs.backupDatabaseReply;
import sirs.remoteDocs.backupDatabaseRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class DatabaseBackupServerImpl extends DatabaseBackupServiceGrpc.DatabaseBackupServiceImplBase {

    @Override
    public void sendDatabaseBackup(backupDatabaseRequest request, StreamObserver<backupDatabaseReply> responseObserver) {
        boolean backupResponse = true;

        ByteArrayOutputStream sqlFileData = new ByteArrayOutputStream();

        ByteString fileChunk = request.getSqlBackupFile();

        int fileChunkSize = fileChunk.size();
        System.out.println("File with size: " + fileChunkSize);

        try {
            File dir = new File("src/main/DatabaseServerStorage");

            FilenameFilter filter = (f, name) -> name.endsWith("sql");
            File[] files = dir.listFiles(filter);

            if (files != null) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            }
            String filePath = "src/main/DatabaseServerStorage/dbServerBackup_" + files.length + ".sql";

            fileChunk.writeTo(sqlFileData);

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            sqlFileData.writeTo(fileOutputStream);
            fileOutputStream.close();

        } catch (Exception e) {
            System.out.println("Database backup server: ");
            e.printStackTrace();
        }

        backupDatabaseReply reply = backupDatabaseReply.newBuilder()
                                        .setBackupResponse(backupResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}