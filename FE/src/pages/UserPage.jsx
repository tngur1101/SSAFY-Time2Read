import { useState, useEffect, useRef } from 'react';
import { format } from 'date-fns';
import { getTimeRecords, getSolved } from '../apis/myApi.jsx';
import ResultButton from '../components/commons/buttons/ResultButton.jsx';
import TranslucentContainer from '../components/commons/containers/TranslucentContainer.jsx';
import WhiteContainer from '../components/commons/containers/WhiteContainer.jsx';
import ResultContent from '../components/commons/ResultContent.jsx';
import ResultTitle from '../components/commons/ResultTitle.jsx';
import Badges from '../components/my/Badges.jsx';
import Book from '../components/my/book/Book.jsx';
import Newspaper from '../components/my/newspaper/Newspaper.jsx';
import RadarChart from '../components/my/RadarChart.jsx';

// {
// 	"records": [
// 		{
// 			"timeAttactTime": str
// 			"playDate": datetime
// 		},
// 		...
// 	]
// }

// {
//   "social": int,
//   "politics": int,
//   "economy": int,
//   "international": int,
//   "culture": int,
//   "sports": int
// }

const UserPage = () => {
  const [timeRecords, setTimeRecords] = useState([]);
  const [solvedCount, setSolvedCount] = useState({
    social: 10,
    politics: 80,
    economy: 20,
    international: 90,
    culture: 40,
    sports: 60,
  });

  useEffect(() => {
    getTimeRecords()
      .then((data) => {
        setTimeRecords(data.records);
        console.log('TimeRecords Data:', data.records);
      })
      .catch((error) => {
        console.error('Error requesting badge:', error);
      });

    getSolved()
      .then((data) => {
        setSolvedCount(data);
        console.log('SolvedCount Data:', data);
      })
      .catch((error) => {
        console.error('Error requesting badge:', error);
      });
  }, []);

  return (
    <>
      <div className="min-h-screen bg-gradient-to-br from-primary-red-1 to-primary-teal-1">
        <div className="w-full max-w-[90%] lg:max-w-[60%] md:max-w-[75%] mx-auto ">
          <h1>마이페이지</h1>
          <div className="relative flex flex-col items-center w-full gap-4">
            {/* 나의 기록 myRecordbox */}
            <TranslucentContainer>
              <ResultTitle title={'나의 기록'} />
              <div className="flex flex-col items-center justify-between w-full gap-6 lg:flex-row lg:justify-center">
                <div className="w-full lg:w-4/12">
                  <WhiteContainer>
                    <ResultTitle title={'타임어택 기록'} />
                    <ResultContent>
                      new! 00:00:00
                      {timeRecords.map((record, i) => (
                        <div key={record.playDate} className="flex justify-between">
                          <div>{`기록 ${i + 1}:`}</div>
                          <div>{record.timeAttackTime}</div>
                          <div>{format(new Date(record.playDate), 'yyyy-MM-dd HH:mm')}</div>
                        </div>
                      ))}
                    </ResultContent>
                  </WhiteContainer>
                </div>
                <div className="w-full lg:w-6/12">
                  <WhiteContainer>
                    <ResultTitle title={'카테고리별 기록'} />
                    <RadarChart solvedCount={solvedCount} />
                  </WhiteContainer>
                </div>
                <div className="w-full lg:w-3/12">
                  <WhiteContainer>
                    <ResultTitle title={'획득한 뱃지'} />
                    <div className="overflow-y-auto h-[200px]">
                      <Badges />
                    </div>
                  </WhiteContainer>
                </div>
              </div>
            </TranslucentContainer>
            {/* 스크랩한 기사 scapedArticlebox */}
            <TranslucentContainer>
              <ResultTitle title={'스크랩한 기사'} />
              <WhiteContainer>
                <Book />
                <div className="flex flex-row">
                  <Newspaper showTitle />
                  <Newspaper showTitle={false} />
                </div>
              </WhiteContainer>
            </TranslucentContainer>
            {/* 다시 시계토끼 쫓아가기 */}
            <div className="flex justify-end w-full">
              <ResultButton>
                <ResultTitle title={'다시 시계토끼 쫓아가기'} />
              </ResultButton>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default UserPage;
