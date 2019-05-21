package Utilities.DataHandlers.InputDataHandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.http.Part;
import DomainModel.DocumentAdministration.Document;
import DomainModel.PatientAdministration.Media;
import Utilities.UtilitiesHolder;

public class FilesUploader {
	public static String UPLOADS_FOLDER = UtilitiesHolder.APPLICATION_PATH + File.separator + "UploadedFiles" + File.separator; 
	
	public FilesUploader() {
		if(!Files.exists(Paths.get(FilesUploader.UPLOADS_FOLDER))) {
			try {
				Files.createDirectory(Paths.get(FilesUploader.UPLOADS_FOLDER));
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public boolean uploadDocument(Part fileToUpload, Document document) {
		OutputStream outstream = null;
	    InputStream filecontent = null;
	    
		try {			    
			outstream = new FileOutputStream(new File(FilesUploader.UPLOADS_FOLDER + document.getDocumentFilePath().toString() + document.getDocumentExtension()));
	        filecontent = fileToUpload.getInputStream();

	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) 
	        	outstream.write(bytes, 0, read);
	    }
		
		catch (IOException e) {
			e.printStackTrace();
			UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(document);
			return false;
	    }
		
		finally {
	        try {
				if(outstream != null) outstream.close();
				if(filecontent != null) filecontent.close();
			} 
	        catch (IOException e) {
				e.printStackTrace();
				UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(document);
				return false;
			}
		}
		
		return true;
	}
	
	public boolean uploaderMedia(Part fileToUpload, Media media) {
		OutputStream outstream = null;
	    InputStream filecontent = null;
	    
		try {			    
			outstream = new FileOutputStream(new File(FilesUploader.UPLOADS_FOLDER + media.getMediaFilePath().toString() + media.getMediaFileExtension()));
	        filecontent = fileToUpload.getInputStream();

	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) 
	        	outstream.write(bytes, 0, read);
	    }
		
		catch (IOException e) {
			e.printStackTrace();
			UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(media);
			return false;
	    }
		
		finally {
	        try {
				if(outstream != null) outstream.close();
				if(filecontent != null) filecontent.close();
			} 
	        catch (IOException e) {
				e.printStackTrace();
				UtilitiesHolder.SESSION_FACTORY_HANDLER.remove(media);
				return false;
			}
		}
		
		return true;
	}
	
	public boolean deleteDocument(Document document) {
		String filePath = FilesUploader.UPLOADS_FOLDER + document.getDocumentFilePath() + document.getDocumentExtension();
		
		try {
			Files.delete(Paths.get(filePath)); 
		}
		catch(IOException exp) {
			exp.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean deleteMedia(Media media) {
		String filePath = FilesUploader.UPLOADS_FOLDER + media.getMediaFilePath() + media.getMediaFileExtension();
		
		try {
			Files.delete(Paths.get(filePath)); 
		}
		catch(IOException exp) {
			exp.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public byte[] downloadDocument(UUID documentId) {
		Document document = UtilitiesHolder.SEARCHES_HANDLER.getDocumentById(documentId);
		
		if(document == null)
			return null;
		
		String filePath = FilesUploader.UPLOADS_FOLDER + document.getDocumentFilePath() + document.getDocumentExtension();
		
		try {
			return Files.readAllBytes(Paths.get(filePath));
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public byte[] downloadMedia(UUID mediaId) {
		Media media = UtilitiesHolder.SEARCHES_HANDLER.getMediaById(mediaId);
		
		if(media == null)
			return null;
		
		String filePath = FilesUploader.UPLOADS_FOLDER + media.getMediaFilePath() + media.getMediaFileExtension();
		
		try {
			return Files.readAllBytes(Paths.get(filePath));
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
}
