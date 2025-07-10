package com.tima.io;

import com.tima.enums.ExamType;
import com.tima.enums.JobStatus;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import com.tima.model.Question;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileReader {
    private final LineNumberReader reader;

    public FileReader(InputStream in) {
        reader = new LineNumberReader(new InputStreamReader(in));
    }

    public boolean hasNext(Question question) throws IOException {
        String line = reader.readLine();

        if (line == null) {
            return false;
        } else if (line.trim().equals("")) {
            return hasNext(question);
        } else {
            String[] fields = line.split(",", -1);
            if (isValidRecord(question, fields, reader.getLineNumber()) && isValidated(question, fields, reader.getLineNumber())) {
                parseRecord(question, fields);
                question.setStatusMessage(null);
            }
            return true;
        }
    }

    private Boolean isValidRecord(Question question, String[] fields, Integer lineNumber) {
        boolean isValid = !StringUtils.isEmpty(fields[0].trim())
                && !StringUtils.isEmpty(fields[1].trim())
                && !StringUtils.isEmpty(fields[2].trim())
                && !StringUtils.isEmpty(fields[3].trim())
                && !StringUtils.isEmpty(fields[4].trim())
                && !StringUtils.isEmpty(fields[5].trim())
                && !StringUtils.isEmpty(fields[6].trim())
                && !StringUtils.isEmpty(fields[7].trim());

        if (!isValid) {
            question.setStatus(JobStatus.FAILED);
            question.setStatusMessage("Failed to parse record at line " + lineNumber + ": One or more data not provided. Please correct.");
        }
        return isValid;
    }

    private Boolean isValidated(Question question, String[] fields, Integer lineNumber) {
        boolean isValid = true;
        StringBuilder message = new StringBuilder("Failed to parse record at line " + lineNumber + ": ");

        if (!EnumUtils.isValidEnum(QuestionDifficultyLevel.class, fields[5].trim())) {
            isValid = false;
            message.append("Only EASY, MEDIUM or HARD values are allowed for difficulty level, ");
        }
        if (!EnumUtils.isValidEnum(QuestionMode.class, fields[6].trim())) {
            isValid = false;
            message.append("Only PRACTICE or EXAM values are allowed for question mode, ");
        }
        if (!EnumUtils.isValidEnum(ExamType.class, fields[7].trim())) {
            isValid = false;
            message.append("Only WAEC or JAMB values are allowed for exam type, ");
        }

        if (!isValid) {
            question.setStatus(JobStatus.FAILED);
            question.setStatusMessage(message.toString());
        }

        return isValid;
    }

    private void parseRecord(Question question, String[] fields) {
        question.setQuestion(fields[0].trim());
        question.setOptions(convertStringArrayToSet(fields));
        question.setAnswer(fields[2].trim());
        question.setSubject(fields[3].trim());
        question.setTopic(fields[4].trim());
        question.setDifficultyLevel(QuestionDifficultyLevel.valueOf(fields[5].trim()));
        question.setMode(QuestionMode.valueOf(fields[6].trim()));
        question.setExamType(ExamType.valueOf(fields[7].trim()));
        question.setStatus(JobStatus.SUCCESSFUL);
    }

    private Set<String> convertStringArrayToSet(String[] fields) {
        String[] optionList = fields[1].trim().split("\\|");
        return new HashSet<>(Arrays.asList(optionList));
    }
}
