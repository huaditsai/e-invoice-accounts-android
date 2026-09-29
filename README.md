# e記帳 (EleInvAccounts)

e記帳 is an Android bookkeeping app built on Taiwan's electronic invoices (e-invoices). A student team built it in 2013 and 2014 at National Taipei University of Education (國立臺北教育大學) during my master's program, from an idea proposed by a fellow master's student.

**Status: archived historical project, no longer maintained.** It has not been built or tested against current Android versions or online services.

## Competition

Entered in the 18th InnoServe Awards (2013 第18屆全國大專校院資訊應用服務創新競賽), a national ICT innovation competition for college students in Taiwan.

- Project post (Traditional Chinese, 2014): https://dotblogs.com.tw/huadi73/2014/05/16/145147

## Features

- Record spending by hand, by scanning the QR codes printed on e-invoices, or by importing invoices from a mobile barcode carrier through the Ministry of Finance e-invoice platform.
- Invoice list, monthly spending by category, automatic prize checking, and past winning numbers.
- A leaderboard that compares prizes won with Facebook friends.

## Source and history

- Imported on 2026-09-29 from the Team Foundation Version Control (TFVC) project `$/EleInvAccounts` on `huadi.visualstudio.com`. Each TFVC changeset is one git commit with its original author, date, and comment.
- Cleaned before publication: the Facebook app secret, the e-invoice platform app ID, and hard-coded test values for a mobile barcode carrier and its verification code are replaced with placeholders; compiled build output (`bin/`) is removed from every commit; and a teammate's email address is replaced with a placeholder. Names are kept.

## License

No open-source license was found. Do not assume the repository grants any license; bundled third-party code such as ZXing and AChartEngine keeps its own license.
