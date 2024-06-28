import React, { useState } from "react";
import {
  ScheduleComponent,
  ViewsDirective,
  ViewDirective,
  Day,
  Week,
  WorkWeek,
  Month,
  Agenda,
  Inject,
  Resize,
  DragAndDrop,
} from "@syncfusion/ej2-react-schedule";

import { DatePickerComponent } from "@syncfusion/ej2-react-calendars";

import { scheduleData } from "../data/dummy";
import { Header } from "../components";

// eslint-disable-next-line react/destructuring-assignment
const PropertyPane = (props) => <div className="mt-5">{props.children}</div>;

const Scheduler = () => {
  const [scheduleObj, setScheduleObj] = useState();

  const change = (args) => {
    scheduleObj.selectedDate = args.value;
    scheduleObj.dataBind();
  };

  const onDragStart = (arg) => {
    // eslint-disable-next-line no-param-reassign
    arg.navigation.enable = true;
  };

  return (
    <div className="m-2 md:m-10 mt-24 p-2 md:p-10 bg-white rounded-3xl">
      <Header category="App" title="Calendar" />
      <ScheduleComponent
        height="650px"
      showQuickInfo={false}
        ref={(schedule) => setScheduleObj(schedule)}
        selectedDate={new Date()}
        eventSettings={{ dataSource: scheduleData }}
        dragStart={onDragStart}
        
      >
        <ViewsDirective>
          {["Day", "Week", "WorkWeek", "Month", "Agenda"].map((item) => (
            <ViewDirective key={item} option={item} />
          ))}
        </ViewsDirective>
        <Inject
          services={[Day, Week, WorkWeek, Month, Agenda, Resize, DragAndDrop]}
        />
      </ScheduleComponent>
      <PropertyPane>
        <table style={{ width: "100%", background: "white" }}>
          <tbody>
            <tr style={{ height: "50px" }}>
              <td style={{ width: "100%" }}>
                <DatePickerComponent
                  value={new Date()}
                  showClearButton={false}
                  placeholder="Hôm nay"
                  floatLabelType="Always"
                  change={change}
                />
              </td>
            </tr>
          </tbody>
        </table>
      </PropertyPane>
    </div>
  );
};

export default Scheduler;


// import * as React from 'react';
// import * as ReactDOM from 'react-dom';
// import { ScheduleComponent, Day, Week, Month, TimelineViews, Inject, ViewsDirective, ViewDirective } from '@syncfusion/ej2-react-schedule';
// import { scheduleData } from './datasource';
// import { L10n, loadCldr } from '@syncfusion/ej2-base';
// import * as localeObj from "../locale.json";
// import * as numberingSystems from '../numberingSystems.json';
// import * as gregorian from '../ca-gregorian.json';
// import * as numbers from '../numbers.json';
// import * as timeZoneNames from '../timeZoneNames.json';
// import * as islamic from '../ca-islamic.json';
// loadCldr(numberingSystems, gregorian, numbers, timeZoneNames, islamic);
// L10n.load(localeObj);
// const Scheduler = () => {
//     const eventSettings = { dataSource: scheduleData };
//     return (<ScheduleComponent height='550px' showQuickInfo={false} selectedDate={new Date(2018, 1, 15)} locale='ar' eventSettings={eventSettings}>
//       <ViewsDirective>
//         <ViewDirective option='Day'/>
//         <ViewDirective option='Week'/>
//         <ViewDirective option='TimelineWorkWeek'/>
//         <ViewDirective option='Month'/>
//       </ViewsDirective>
//       <Inject services={[Day, Week, Month, TimelineViews]}/>
//     </ScheduleComponent>);
// }
// ;
// const root = ReactDOM.createRoot(document.getElementById('schedule'));
// root.render(<Scheduler />);