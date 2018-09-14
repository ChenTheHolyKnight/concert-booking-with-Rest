package nz.ac.auckland.concert.client.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AWS {
    // AWS S3 access credentials for concert images.
    private static final String AWS_ACCESS_KEY_ID = "AKIAJOG7SJ36SFVZNJMQ";
    private static final String AWS_SECRET_ACCESS_KEY = "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";

    // Name of the S3 bucket that stores images.
    private static final String AWS_BUCKET = "concert2.aucklanduni.ac.nz";

    private AmazonS3 _s3;

    public AWS(){
        // Create an AmazonS3 object that represents a connection with the
        // remote S3 service.
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        _s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.AP_SOUTHEAST_2)
                .withCredentials(
                        new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public Image fetchImage(String imageName){
        if(!checkImageNameExsists(imageName)){
            return null;
        }else {
            File imageFile = download(_s3, imageName);
            //Image image = Toolkit.getDefaultToolkit().getImage(String.valueOf(imageFile));
            Image image= null;
            try {
                image = ImageIO.read(imageFile);
                return image;
            } catch (IOException e) {
                return null;
            }

        }

    }

    private boolean checkImageNameExsists(String imageName){
        List<String> imageNames=getImageNames(_s3);
        if(imageNames.contains(imageName)){
            return true;
        }else {
            return false;
        }
    }

    public AmazonS3 getAmazon(){
        return _s3;
    }

    /**
     * Finds image names stored in a bucket named AWS_BUCKET.
     *
     * @param s3 the AmazonS3 connection.
     * @return a List of images names.
     */
    private static List<String> getImageNames(AmazonS3 s3) {

        List<String> imageNames = new ArrayList<>();
        ObjectListing listing = s3.listObjects(AWS_BUCKET);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        for (S3ObjectSummary summary : summaries) {
            imageNames.add(summary.getKey());
        }

        System.out.println("Detected " + imageNames.size() + " files for download.");
        return imageNames;
    }

    /**
     * Downloads images in the bucket named AWS_BUCKET.
     *
     * @param s3         the AmazonS3 connection.
     * @param imageName the named images to download.
     */
    private static File download(AmazonS3 s3, String imageName) {
        File imageFile = new File(imageName);
        System.out.print("Downloading " + imageName + "... ");
        GetObjectRequest req = new GetObjectRequest(AWS_BUCKET, imageName);
        s3.getObject(req, imageFile);
        System.out.println("Complete!");

        return imageFile;
    }
}
