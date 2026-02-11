import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'gameModeFormat'
})
export class GameModeFormatPipe implements PipeTransform {
  transform(value: string): string {
    switch (value) {
      case 'MID_ONLY_1V1':
        return "Mid only (1v1)";
      case 'MID_ONLY_2V2':
        return "Mid only (2v2)";
      case 'CAPTAINS_MODE_5V5':
        return "Captains Mode (5v5)";
      case 'ALL_PICK_5V5':
        return 'All Pick (5v5)';
      case 'RANDOM_DRAFT_5V5':
        return 'Random Draft (5v5)';
      default:
        return value;
    }
  }
}