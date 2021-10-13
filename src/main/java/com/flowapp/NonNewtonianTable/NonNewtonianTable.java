package com.flowapp.NonNewtonianTable;

import com.flowapp.NonNewtonianTable.Models.LineRow;
import com.flowapp.NonNewtonianTable.Utils.FileUtils;
import com.flowapp.NonNewtonianTable.Utils.TableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NonNewtonianTable {

    private StringBuilder steps;

    public List<LineRow> nonNewtonianTable(float startQM3H,
                                           float endQM3H,
                                           float deltaQM3H,
                                           float spGr,
                                           float iDmm,
                                           float lengthM,
                                           float hs,
                                           float nDash,
                                           float kDash,
                                           float anDash,
                                           float bnDash) {

        final float iDm = iDmm / 1000;
        final float density = spGr * 1000;

        final List<LineRow> rows = new ArrayList<>();

        for (float q = startQM3H; q <= endQM3H; q += deltaQM3H) {
            final float v = (float) (4 * q / (Math.PI * Math.pow(iDm, 2) * 3600));
            final float nre = (float) (Math.pow(iDm, nDash) * Math.pow(v, 2-nDash) * density / (kDash * Math.pow(8, nDash - 1)));
            final float f;
            if (nre != 0) {
                f = (float) (anDash / Math.pow(nre, bnDash));
            } else {
                f = 0;
            }
            final float hf = (float) (f * lengthM * Math.pow(v,2) / (2 * 9.8 * iDm));
            final float ht = hf + hs;
            final var line = new LineRow(q, v, nre, f, hf, ht);
            rows.add(line);
        }
        return rows;
    }

    private void renderTable(List<Object[]> args) {
        renderTable(args.toArray(new Object[0][0]));
    }

    private void renderTable(Object[] ... args) {
        final var temp = args[0];
        final String[] firstRow = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            firstRow[i] = temp[i].toString();
        }
        TableList at = new TableList(firstRow).withUnicode(true);
        final var newRows = Arrays.stream(args).skip(1).map(row -> {
            final String[] newRow = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                final Object object = row[i];
                if (object instanceof Number) {
                    newRow[i] = formatNumber((Number) object);
                } else {
                    newRow[i] = object.toString();
                }
            }
            return newRow;
        }).collect(Collectors.toList());
        for (var row: newRows) {
            at.addRow(row);
        }
        String rend = at.render();
        println(rend);
    }

    private void println(String pattern, Object... args) {
        final String message = format(pattern, args);
        steps.append(message).append('\n');
        FileUtils.printOut(message);
    }

    private void clear() {
        steps = new StringBuilder();
        FileUtils.clear();
    }

    private String formatNumber(Number number) {
        final var value = number.floatValue();
        if (number instanceof Double) {
            return number.toString();
        }
        if (value < 0) {
            return String.format("%.7f", value);
        } else if (value == 0) {
            return  "0";
        } else {
            return String.format("%.4f", value).replace(".0000", "");
        }
    }

    private String format(String pattern, Object... args) {
        Pattern rePattern = Pattern.compile("\\{([0-9+-]*)}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = rePattern.matcher(pattern);
        int counter = -1;
        while (matcher.find()) {
            counter++;
            String number = matcher.group(1);
            if (number == null) {
                number = "";
            }
            if (!number.isBlank()) {
                if (number.equals("+")) {
                    number = "\\+";
                    counter++;
                } else if (number.equals("-")) {
                    counter--;
                } else {
                    counter = Integer.parseInt(number);
                }
            }
            counter = clamp(counter, 0, args.length - 1);
            String toChange = "\\{" + number + "}";
            Object object = args[counter];
            String objectString;
            if (object instanceof Number) {
                objectString = formatNumber((Number) object);
            } else {
                objectString = object.toString();
            }
            String result = objectString;
            pattern = pattern.replaceFirst(toChange, result);
        }
        return pattern;
    }

    private <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }
}
