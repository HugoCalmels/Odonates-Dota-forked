import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatTeamDate'
})
export class FormatTeamDatePipe implements PipeTransform {

  transform(value: string | Date | null | undefined): string | null {
    if (!value) {
      return null;
    }
    
    let date: Date;
    if (typeof value === 'string') {
      date = new Date(value);
    } else if (value instanceof Date) {
      date = value;
    } else {
      return null;
    }

    if (isNaN(date.getTime())) {
      return null;
    }

    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day} / ${month} / ${year}`;
  }

}