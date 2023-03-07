package org.acad;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import models.Enrollment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "report", mixinStandardHelpOptions = true, description = "Generates transcripts and graduation reports for students")
public class Report implements Callable<Integer> {
    @Option(names = {"-F", "--File"}, description = "File name(In csv containing Entry Numbers)", paramLabel = "File name", descriptionKey = "File name", interactive = true, echo = true, prompt = "File name: ", defaultValue = "", arity = "0..1")
    String fileName;

    public HashMap<String, String> calcSGPA(ArrayList<String> sessionMap) {
        Map<String, String> output = new HashMap<String, String>();
        float credits = 0;
        float cumCredits = 0;
        float cumPoints = 0;
        float points = 0;
        float faliedCredits = 0;
        for (int i = 0; i < sessionMap.size(); i += 4) {
            String grade = sessionMap.get(i + 3);
            float creditsTemp = Float.parseFloat(sessionMap.get(i + 2));
            if (grade.equals("A")) {
                points += 10 * creditsTemp;
                cumPoints += 10 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("A-")) {
                points += 9 * creditsTemp;
                cumPoints += 9 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("B")) {
                points += 8 * creditsTemp;
                cumPoints += 8 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("B-")) {
                points += 7 * creditsTemp;
                cumPoints += 7 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("C")) {
                points += 6 * creditsTemp;
                cumPoints += 6 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("C-")) {
                points += 5 * creditsTemp;
                cumPoints += 5 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("D")) {
                points += 4 * creditsTemp;
                cumPoints += 4 * creditsTemp;
                cumCredits += creditsTemp;
                credits += creditsTemp;
            } else if (grade.equals("E")) {
                points += 2 * creditsTemp;
                faliedCredits += creditsTemp;
            } else if (grade.equals("F")) {
                points += 0;
                faliedCredits += creditsTemp;
            }
        }
        String sgpa = "";
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if ((credits + faliedCredits) != 0) {
            sgpa = String.valueOf(df.format(points / (credits + faliedCredits)));
        }
        output.put("SGPA", sgpa);
        output.put("CumCredits", String.valueOf(cumCredits));
        output.put("CumPoints", String.valueOf(cumPoints));
        output.put("Credits", String.valueOf(credits));
        output.put("Points", String.valueOf(points));
        return (HashMap<String, String>) output;
    }

    public boolean checkGraduation(Map<String, String> reportRecord, Map<String, Float> creditsMap) {
        float extras = 0;
        boolean isEligible = true;
        for (Map.Entry<String, Float> entry : creditsMap.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            if (key.equals("OE")) {
                continue;
            }
            if (value < Float.parseFloat(reportRecord.get(key + "(Required)"))) {
                isEligible = false;
            } else {
                extras += value - Float.parseFloat(reportRecord.get(key + "(Required)"));
            }
        }
        creditsMap.put("OE", creditsMap.get("OE") + extras);
        if (creditsMap.get("OE") < Float.parseFloat(reportRecord.get("OE(Required)"))) {
            isEligible = false;
        }
        return isEligible;
    }

    @Override
    public Integer call() {
        try {
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new java.io.FileReader(fileName));
            CSVWriter writer = new CSVWriter(new java.io.FileWriter("report.csv"));

            writer.writeNext(new String[]{"Entry Number", "Name", "Program",
                    "Department", "SC(Required)", "SC(Completed)", "SE(Required)",
                    "SE(Completed)", "GE(Required)", "GE(Completed)", "PC(Required)",
                    "PC(Completed)", "PE(Required)", "PE(Completed)", "HC(Required)",
                    "HC(Completed)", "HE(Required)", "HE(Completed)", "II(Required)",
                    "II(Completed)", "CP(Required)", "CP(Completed)", "EC(Required)",
                    "EC(Completed)", "OE(Required)", "OE(Completed)", "Total Credits",
                    "CGPA", "Graduation Status"});
            Map<String, String> record;

            ArrayList<models.Enrollment> enrollments = models.Enrollment.retrieveAll();
            Map<String, ArrayList<models.Enrollment>> enrollmentsMap = new HashMap<String, ArrayList<Enrollment>>();
            for (models.Enrollment e : enrollments) {
                if (enrollmentsMap.containsKey(e.getEntryNo())) {
                    enrollmentsMap.get(e.getEntryNo()).add(e);
                } else {
                    ArrayList<models.Enrollment> temp = new ArrayList<models.Enrollment>();
                    temp.add(e);
                    enrollmentsMap.put(e.getEntryNo(), temp);
                }
            }
            ArrayList<models.Offering> offerings = models.Offering.retrieveAll();
            while ((record = reader.readMap()) != null) {
                Map<String, String> reportRecord = new HashMap<String, String>();
                String entryNumber = record.get("Entry Number");
                reportRecord.put("Entry Number", entryNumber);
                ArrayList<models.Curriculum> curriculums = models.Curriculum.retrieveAll();
                models.Student student = models.Student.retrieve(entryNumber);
                if (student == null) {
                    System.err.println("Student with entry number " + entryNumber + " not found");
                    continue;
                }
                reportRecord.put("Name", student.getName());
                reportRecord.put("Program", student.getProgram());
                reportRecord.put("Department", student.getDepartmentCode());
                reportRecord.put("SC(Required)", "0");
                reportRecord.put("SC(Completed)", "0");
                reportRecord.put("SE(Required)", "0");
                reportRecord.put("SE(Completed)", "0");
                reportRecord.put("GE(Required)", "0");
                reportRecord.put("GE(Completed)", "0");
                reportRecord.put("PC(Required)", "0");
                reportRecord.put("PC(Completed)", "0");
                reportRecord.put("PE(Required)", "0");
                reportRecord.put("PE(Completed)", "0");
                reportRecord.put("HC(Required)", "0");
                reportRecord.put("HC(Completed)", "0");
                reportRecord.put("HE(Required)", "0");
                reportRecord.put("HE(Completed)", "0");
                reportRecord.put("II(Required)", "0");
                reportRecord.put("II(Completed)", "0");
                reportRecord.put("CP(Required)", "0");
                reportRecord.put("CP(Completed)", "0");
                reportRecord.put("EC(Required)", "0");
                reportRecord.put("EC(Completed)", "0");
                reportRecord.put("OE(Required)", "0");
                reportRecord.put("OE(Completed)", "0");
                reportRecord.put("Total Credits", "0");
                reportRecord.put("CGPA", "0");
                reportRecord.put("Graduation Status", "Ineligible");
                for (models.Curriculum c : curriculums) {
                    if (c.getProgram().equals(student.getProgram())) {
                        reportRecord.put(c.getCourseType() + "(Required)", String.valueOf(c.getMinCredits()));
                    }
                }
                HashMap<String, Float> creditsMap = new HashMap<String, Float>();
                creditsMap.put("SC", 0f);
                creditsMap.put("SE", 0f);
                creditsMap.put("GE", 0f);
                creditsMap.put("PC", 0f);
                creditsMap.put("PE", 0f);
                creditsMap.put("HC", 0f);
                creditsMap.put("HE", 0f);
                creditsMap.put("II", 0f);
                creditsMap.put("CP", 0f);
                creditsMap.put("EC", 0f);
                creditsMap.put("OE", 0f);

                models.Offering offering = null;
                float cumCredits = 0;
                float totalPoints = 0;
                Map<String, ArrayList<String>> sessionMap = new HashMap<String, ArrayList<String>>();
                if (enrollmentsMap.get(entryNumber) == null) {
                    continue;
                }
                for (models.Enrollment e : enrollmentsMap.get(entryNumber)) {

                    for (models.Offering o : offerings) {
                        if (o.getId() == e.getId()) {
                            offering = o;
                            break;
                        }
                    }

                    models.Catalog catalog = models.Catalog.retrieve(offering.getCode());
                    if (e.getGrade().equals("A") ||
                            e.getGrade().equals("A-") ||
                            e.getGrade().equals("B") ||
                            e.getGrade().equals("B-") ||
                            e.getGrade().equals("C") ||
                            e.getGrade().equals("C-") ||
                            e.getGrade().equals("D")
                    ) {
                        creditsMap.put(e.getCourseType(), creditsMap.get(e.getCourseType()) + catalog.getCredits());
                    }
                    String session = String.join("-", String.valueOf(offering.getYear()), offering.getSemester());
                    if (sessionMap.containsKey(session)) {
                        sessionMap.get(session).add(catalog.getName());
                        sessionMap.get(session).add(catalog.getCode());
                        sessionMap.get(session).add(String.valueOf(catalog.getCredits()));
                        sessionMap.get(session).add(e.getGrade());

                    } else {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(catalog.getName());
                        temp.add(catalog.getCode());
                        temp.add(String.valueOf(catalog.getCredits()));
                        temp.add(e.getGrade());
                        sessionMap.put(session, temp);
                    }
                }
                for (String courseType : creditsMap.keySet()) {
                    reportRecord.put(courseType + "(Completed)", String.valueOf(creditsMap.get(courseType)));
                }
                ArrayList<String> sortedSessions = new ArrayList<String>();
                for (String session : sessionMap.keySet()) {
                    sortedSessions.add(session);
                }
                sortedSessions.sort(null);
                for (String session : sortedSessions) {
                    Map<String, String> semDetails = calcSGPA(sessionMap.get(session));
                    totalPoints += Float.parseFloat(semDetails.get("CumPoints"));
                    cumCredits += Float.parseFloat(semDetails.get("CumCredits"));
                    String cgpa = "0";
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    if (cumCredits != 0) {
                        cgpa = String.valueOf(df.format(totalPoints / cumCredits));
                    }
                    InputStream inputStream = getClass().getResourceAsStream("/gradesheet.html");
                    String html = new String(inputStream.readAllBytes());
                    html = html.replace("{session}", session);
                    html = html.replace("{name}", student.getName());
                    html = html.replace("{entry_number}", entryNumber);
                    html = html.replace("{department}", student.getDepartmentCode());
                    html = html.replace("{institute_name}", "Indian Institute of Technology, Ropar");
                    html = html.replace("{sgpa}", semDetails.get("SGPA"));
                    html = html.replace("{cumulative_credits}", String.valueOf(cumCredits));
                    html = html.replace("{earned_credits}", semDetails.get("Credits"));
                    html = html.replace("{cgpa}", cgpa);
                    html = html.replace("{program}", student.getProgram());
                    html = html.replace("{date}", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                    ArrayList<String> courses = sessionMap.get(session);
                    String courseHtml = "";
                    ArrayList<String> takes = sessionMap.get(session);
                    for (int i = 0; i < takes.size(); i += 4) {
                        String name = takes.get(i + 1);
                        String code = takes.get(i);
                        String credits = takes.get(i + 2);
                        String grade = takes.get(i + 3);
                        courseHtml += String.join("", "<tr><td>", name, "</td><td>", code, "</td><td>", credits, "</td><td>", grade, "</td></tr>");
                    }
                    html = html.replace("{#courses}", courseHtml);
                    html = html.replace("{/courses}", "");
                    InputStream logoInputStream = getClass().getResourceAsStream("/logo.png");
                    byte[] logoBytes = logoInputStream.readAllBytes();
                    String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);
                    String logoDataUrl = "data:image/png;base64," + logoBase64;
                    html = html.replace("{logo}", logoDataUrl);
                    Document document = Jsoup.parse(html);
                    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                    ITextRenderer renderer = new ITextRenderer();
                    SharedContext sharedContext = renderer.getSharedContext();
                    sharedContext.setPrint(true);
                    sharedContext.setInteractive(false);
                    renderer.setDocumentFromString(document.html());
                    renderer.layout();
                    File file = new File("./Transcripts");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String outputFileName = "./Transcripts/" + String.join("-", entryNumber, session) + ".pdf";
                    OutputStream outputStream = new FileOutputStream(outputFileName);
                    renderer.createPDF(outputStream);
                }
                String cgpa = "0";
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                if (cumCredits != 0) {
                    cgpa = String.valueOf(df.format(totalPoints / cumCredits));
                }
                reportRecord.put("CGPA", cgpa);
                reportRecord.put("Total Credits", String.valueOf(cumCredits));
                if (checkGraduation(reportRecord, creditsMap) && Float.parseFloat(cgpa) >= 5.0) {
                    reportRecord.put("Graduation Status", "Eligible");
                }
                String[] row = {reportRecord.get("Entry Number"),
                        reportRecord.get("Name"),
                        reportRecord.get("Program"),
                        reportRecord.get("Department"),
                        reportRecord.get("SC(Required)"),
                        String.valueOf(creditsMap.get("SC")),
                        reportRecord.get("SE(Required)"),
                        String.valueOf(creditsMap.get("SE")),
                        reportRecord.get("GE(Required)"),
                        String.valueOf(creditsMap.get("GE")),
                        reportRecord.get("PC(Required)"),
                        String.valueOf(creditsMap.get("PC")),
                        reportRecord.get("PE(Required)"),
                        String.valueOf(creditsMap.get("PE")),
                        reportRecord.get("HC(Required)"),
                        String.valueOf(creditsMap.get("HC")),
                        reportRecord.get("HE(Required)"),
                        String.valueOf(creditsMap.get("HE")),
                        reportRecord.get("II(Required)"),
                        String.valueOf(creditsMap.get("II")),
                        reportRecord.get("CP(Required)"),
                        String.valueOf(creditsMap.get("CP")),
                        reportRecord.get("EC(Required)"),
                        String.valueOf(creditsMap.get("EC")),
                        reportRecord.get("OE(Required)"),
                        String.valueOf(creditsMap.get("OE")),
                        reportRecord.get("Total Credits"),
                        reportRecord.get("CGPA"),
                        reportRecord.get("Graduation Status")
                };
                writer.writeNext(row);
            }
            writer.close();
            System.out.println("Transcripts generated successfully and saved at " + new File("Transcripts").getAbsolutePath());
            System.out.println("Report generated successfully and saved at " + new File("Report.csv").getAbsolutePath());
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while generating report.");
            return UNKNOWN;
        }
    }
}
