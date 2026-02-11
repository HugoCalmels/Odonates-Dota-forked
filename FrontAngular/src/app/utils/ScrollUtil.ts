
export default class ScrollUtil {
    //t = current time
    //b = start value
    //c = change in value
    //d = duration
    static easeInOutQuad = function (t: any, b: any, c: any, d: any) {
        t /= d/2;
        if (t < 1) return c/2*t*t + b;
        t--;
        return -c/2 * (t*(t-2) - 1) + b;
    };

    static scrollTo(element: any, to: any, duration: any) {
        let start = element.scrollTop,
            change = to - start,
            currentTime = 0,
            increment = 10;

        let animateScroll = function(){
            currentTime += increment;
            element.scrollTop = ScrollUtil.easeInOutQuad(currentTime, start, change, duration);
            if(currentTime < duration) {
                setTimeout(animateScroll, increment);
            }
        };
        animateScroll();
    }
}
