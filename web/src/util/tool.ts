export class Tool {

    public static isEmpty (obj: any) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }


    public static isNotEmpty (obj: any) {
        return !this.isEmpty(obj);
    }


    public static copy (obj: object) {
        if (Tool.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj));
        }
    }
}