import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'mmrEstimate'
})
export class MmrEstimatePipe implements PipeTransform {

  private readonly medalMapping: { [key: string]: string } = {
    '11': 'herald 1',
    '12': 'herald 2',
    '13': 'herald 3',
    '14': 'herald 4',
    '15': 'herald 5',
    '21': 'guardian 1',
    '22': 'guardian 2',
    '23': 'guardian 3',
    '24': 'guardian 4',
    '25': 'guardian 5',
    '31': 'crusader 1',
    '32': 'crusader 2',
    '33': 'crusader 3',
    '34': 'crusader 4',
    '35': 'crusader 5',
    '41': 'archon 1',
    '42': 'archon 2',
    '43': 'archon 3',
    '44': 'archon 4',
    '45': 'archon 5',
    '51': 'legend 1',
    '52': 'legend 2',
    '53': 'legend 3',
    '54': 'legend 4',
    '55': 'legend 5',
    '61': 'ancient 1',
    '62': 'ancient 2',
    '63': 'ancient 3',
    '64': 'ancient 4',
    '65': 'ancient 5',
    '71': 'divine 1',
    '72': 'divine 2',
    '73': 'divine 3',
    '74': 'divine 4',
    '75': 'divine 5',
    '80': 'immortal' 
  };

  transform(element: any): string {
    if (!element || !element.rankTier) {
      return '';
    }

    console.log('Processing element:', element);
  
    if (String(element.rankTier) === '80') {
      
      if (element.leaderboardRank && element.leaderboardRank !== null) {
        if (element.leaderboardRank <= 10) {
          return '../assets/img/medals/immortal/immortal 4.png';
        }
        if (element.leaderboardRank <= 1000) {
          return '../assets/img/medals/immortal/immortal 3.png';
        }
      }
  
      return '../assets/img/medals/immortal/immortal 1.png';
    }
  
    const medal = this.medalMapping[element.rankTier];
    
    if (medal) {
      const path = `../assets/img/medals/${medal.split(' ')[0]}/${medal}.png`;
      return path;
    }

    return '';
  }
}