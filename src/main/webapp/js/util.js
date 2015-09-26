function toNiceDate(jsondate) {
  date = new Date(jsondate);
	
  year = "" + date.getFullYear();
  month = "" + (date.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
  day = "" + date.getDate(); if (day.length == 1) { day = "0" + day; }
  hour = "" + date.getHours(); if (hour.length == 1) { hour = "0" + hour; }
  minute = "" + date.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
  second = "" + date.getSeconds(); if (second.length == 1) { second = "0" + second; }
  return day + "." + month + "." + year;
}