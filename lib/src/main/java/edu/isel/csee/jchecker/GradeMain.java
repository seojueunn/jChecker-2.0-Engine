package edu.isel.csee.jchecker;

import edu.isel.csee.jchecker.core.CoreGrader;
import edu.isel.csee.jchecker.submit.Extractor;
import org.apache.commons.cli.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GradeMain {
    private String ipath;
    private String jpath;
    private String opath;
    private boolean help;
    private String policy;
    private String result;

    public static void main(String[] args) {
        GradeMain grade = new GradeMain();

        grade.run(args);
    }

    public void run(String[] gradeOptions) {
        Options options = createOptions();

        if (parseOptions(options, gradeOptions)){
            // option is -h or -help
            if (help){
                printHelp(options);
                return;
            }

            // preprocessing
            try {
                opath = Files.createTempDirectory("jchecker").toAbsolutePath().toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (opath == null) System.out.println("Cannot create Temp Directory");

            new Extractor().unzip(ipath, opath);

            // grading policy
            try {
                policy = jsonToString(jpath);
            } catch (Exception e){
                e.printStackTrace();
            }

            // for MAC
            deleteTrashFile(opath);

            // grading
            result = new CoreGrader().start(opath, policy);

            System.out.println("****************************************");
            System.out.println("RESULT: " + result);

            deleteFile(opath);
        }
    }

    public String jsonToString(String jpath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(jpath)));
    }

    public void deleteTrashFile(String crtPath)
    {
        File FileList = new File(crtPath);

        String fileList[] = FileList.list();

        for(int i = 0; i < fileList.length; i++)
        {
            String FileName = fileList[i];

            if (FileName.contains("__MACOSX")) deleteFile(crtPath + FileName);
        }
    }

    public void deleteFile(String path)
    {
        File deleteFolder = new File(path);

        if(deleteFolder.exists())
        {
            File[] deleteFolderList = deleteFolder.listFiles();

            for (int i = 0; i < deleteFolderList.length; i++)
            {
                if(deleteFolderList[i].isFile()) { deleteFolderList[i].delete(); }
                else { deleteFile(deleteFolderList[i].getPath()); }

                deleteFolderList[i].delete();
            }
            deleteFolder.delete();
        }
    }

    private boolean parseOptions(Options options, String[] args){
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            ipath = cmd.getOptionValue("i");
            jpath = cmd.getOptionValue("j");
            help = cmd.hasOption("h");

        } catch (Exception e) {
            printHelp(options);
            return false;
        }

        return true;
    }

    private Options createOptions(){
        Options options = new Options();

        options.addOption(Option.builder("h").longOpt("help")
                .desc("Show a Help page")
                .build());

        options.addOption(Option.builder("i").longOpt("input")
                .desc("Set an input data file path for code")
                .hasArg()
                .argName("Input file path")
                .required()
                .build());

        options.addOption(Option.builder("j").longOpt("json")
                .desc("Set an policy file for grading")
                .hasArg()
                .argName("JSON file path")
                .required()
                .build());

        return options;
    }

    private void printHelp(Options options) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        String header = "Grading program";
        String footer ="\nThis program is jChecker CLI version.";
        formatter.printHelp("jChecker", header, options, footer, true);
    }
}
