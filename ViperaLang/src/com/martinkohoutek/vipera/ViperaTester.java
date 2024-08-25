package com.martinkohoutek.vipera;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class ViperaTester {

    private static final String GREEN = "\033[0;32m";
    private static final String RED = "\033[0;31m";
    private static final String RESET = "\033[0m";

    public static void main(String[] args) {
        String directoryPath = "tests";

        try {
            List<Path> testFiles = Files.list(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".in"))
                    .sorted()  // Sort files to ensure correct order
                    .toList();

            int testNumber = 1;
            for (Path testFile : testFiles) {
                Path expectedFile = Paths.get(directoryPath, testFile.getFileName().toString().replace(".in", ".out"));

                String testFileName = testFile.getFileName().toString();
                String resultLine = "test " + String.format("%02d", testNumber) + " (" + testFileName + "): ";

                if (Files.exists(expectedFile)) {
                    try {
                        Process process = new ProcessBuilder("java", "-cp", "out/production/ViperaLang", "com.martinkohoutek.vipera.Vipera", testFile.toString()).start();
                        BufferedReader programOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        StringBuilder actualOutput = new StringBuilder();
                        String line;
                        while ((line = programOutput.readLine()) != null) {
                            actualOutput.append(line).append("\n");
                        }
                        process.waitFor();

                        ComparisonResult result = compareOutputs(expectedFile.toString(), actualOutput.toString());
                        if (result.isEqual()) {
                            resultLine += GREEN + "PASS" + RESET;
                        } else {
                            resultLine += RED + "FAIL" + RESET + String.format("    Line: %d    Expected: %-20s  Returned: %-20s", result.getLine(), result.getExpected(), result.getActual());
                        }
                    } catch (IOException e) {
                        resultLine += "Error comparing files: " + e.getMessage();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        resultLine += "Error running the program: " + e.getMessage();
                    }
                } else {
                    resultLine += "Output file does not exist";
                }

                System.out.println(resultLine);

                testNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
        }
    }

    private static ComparisonResult compareOutputs(String expectedFile, String actualOutput) throws IOException {
        try (BufferedReader expectedReader = new BufferedReader(new FileReader(expectedFile))) {
            String expectedLine;
            String[] actualLines = actualOutput.split("\n");

            int lineNum = 0;
            while ((expectedLine = expectedReader.readLine()) != null) {
                if (lineNum >= actualLines.length || !expectedLine.trim().equals(actualLines[lineNum].trim())) {
                    String actualLine = lineNum < actualLines.length ? actualLines[lineNum] : "No output";
                    return new ComparisonResult(false, expectedLine.trim(), actualLine.trim(), lineNum + 1);
                }
                lineNum++;
            }

            if (lineNum < actualLines.length) {
                String actualLine = actualLines[lineNum];
                return new ComparisonResult(false, "No output", actualLine.trim(), lineNum + 1);
            }

            return new ComparisonResult(true, "", "", 0);
        }
    }

    private static class ComparisonResult {
        private final boolean equal;
        private final String expected;
        private final String actual;
        private final int line;

        ComparisonResult(boolean equal, String expected, String actual, int line) {
            this.equal = equal;
            this.expected = expected;
            this.actual = actual;
            this.line = line;
        }

        boolean isEqual() {
            return equal;
        }

        String getExpected() {
            return expected;
        }

        String getActual() {
            return actual;
        }

        int getLine() {
            return line;
        }
    }
}