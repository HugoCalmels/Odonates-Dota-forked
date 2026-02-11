import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateTimeFormat'
})
export class DateTimeFormatPipe implements PipeTransform {
  transform(dateTime: number[] | Date): string {
    const date = Array.isArray(dateTime) ? new Date(dateTime[0], dateTime[1] - 1, dateTime[2], dateTime[3], dateTime[4]) : dateTime;
    const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    const monthsOfYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

    const day = date.getDate();
    const monthIndex = date.getMonth();
    const month = monthsOfYear[monthIndex];
    const year = date.getFullYear();
    const hours = date.getHours();
    const minutes = date.getMinutes();

    const formattedDate = `${daysOfWeek[date.getDay()]} ${day} ${month} ${year} - ${hours}h${minutes.toString().padStart(2, '0')}`;
    return formattedDate;
  }
}