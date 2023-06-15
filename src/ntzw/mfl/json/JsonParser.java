package ntzw.mfl.json;

public class JsonParser {

    private char[] charsBuffer;

    public JsonParser() {
        this(16384);
    }

    public JsonParser(int charBufferSize) {
        charsBuffer = new char[charBufferSize];
    }

    public JsonObject getRootObject(String jsonString) {
        try {
            return parseObject(new CharBuffer(jsonString));
        } catch (Exception e) {
            System.err.println("Unable to parse given JSON string!");
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject parseObject(CharBuffer buffer) {
        JsonObject object = new JsonObject();
        String name = "";
        char c;
        while((c = buffer.nextNonWhitespace()) != 0) {
            switch (c) {
                case '\\':
                    buffer.skip(1);
                    break;
                case '"':
                    name = parseString(buffer);
                    //object.setName(name);
                    break;
                case ':':
                    JsonObject field = parseValue(buffer);
                    field.setName(name);
                    object.addField(field);
                    break;
                case '}':
                    return object;
            }
        }
        return object;
    }

    //buffer position after opening quotes
    private String parseString(CharBuffer buffer) {
        int nameLength = 0;
        boolean backslashed = false;
        char c;
        for(int i = 0; i < charsBuffer.length; i++) {
            c = buffer.next();
            if(!backslashed && c == '\\') {
                backslashed = true;
                continue;
            }
            if(!backslashed && c == '"') break;
            charsBuffer[nameLength++] = c;
            backslashed = false;
        }
        return new String(charsBuffer, 0, nameLength);
    }

    private JsonObject parseValue(CharBuffer buffer) {
        switch (buffer.nextNonWhitespace()) {
            case '"':
                return new JsonString(parseString(buffer));
            case 't':
                buffer.skip(3); // 't'rue
                return new JsonBoolean(true);
            case 'f':
                buffer.skip(4); // 'f'alse
                return new JsonBoolean(false);
            case 'n':
                buffer.skip(3); // 'n'ull
                return new JsonNull();
            case '{':
                return parseObject(buffer);
            case '[':
                return parseArray(buffer);
            default:
                buffer.setPosition(buffer.getPosition() - 1);
                return parseNumber(buffer);

        }
    }

    private JsonArray parseArray(CharBuffer buffer) {
        JsonArray array = new JsonArray();
        char c;
        while((c = buffer.nextNonWhitespace()) != 0) {
            switch(c) {
                case ',':
                    continue;
                case ']':
                    return array;
                default:
                    buffer.setPosition(buffer.getPosition() - 1);
                    array.addField(parseValue(buffer));
                    break;
            }
        }
        return array;
    }

    private JsonNumber parseNumber(CharBuffer buffer) {
        char c = buffer.nextNonWhitespace();
        int numberDigits = 0;
        do {
            if(c == ',' || c == ']' || c == '}') {
                break;
            }
            charsBuffer[numberDigits++] = c;
        } while((c = buffer.next()) != 0);
        buffer.setPosition(buffer.getPosition() - 1);
        return new JsonNumber(Double.parseDouble(new String(charsBuffer, 0, numberDigits).trim()));
    }
}
