import { useState } from "react";
// import { AgGridReact } from "ag-grid-react"; // React Grid Logic
import "ag-grid-community/styles/ag-grid.css"; // Core CSS
import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme
// import { vector } from "../../assets/images/svg/SVGIcons";
// import InputSelectField from "../../components/InputSelectFiled";
import { graphSVG } from "../../assets/images/svg/SVGIcons";
import HorizontalBarChart from "../../components/HorizontalBarChart";
import { handleValue } from "../../utilities/FormatUtilities";
import "./CareProvider.css";

const CareProviderDetails = () => {
  const [showGraph, setShowGraph] = useState(true);

  // const membersbyProviderData = [
  //   {
  //     noOfMembers: "Provider Group 1",
  //     topMembers: 13.4,
  //   },
  //   {
  //     noOfMembers: "Provider Group 2",
  //     topMembers: 1.4,
  //   },
  //   {
  //     noOfMembers: "Provider Group 3",
  //     topMembers: 3.4,
  //   },
  //   {
  //     noOfMembers: "Provider Group 4",
  //     topMembers: 0.4,
  //   },
  //   {
  //     noOfMembers: "Provider Group 5",
  //     topMembers: 11.4,

  //   },
  //   {
  //     noOfMembers: "Provider Group 6",
  //     topMembers: 15.4,

  //   },
  //   {
  //     noOfMembers: "Provider Group 7",
  //     topMembers: 13.4,

  //   },
  //   {
  //     noOfMembers: "Provider Group 8",
  //     topMembers: 13.4,

  //   },
  //   {
  //     noOfMembers: "Provider Group 9",
  //     topMembers: 13.4,

  //   },
  //   {
  //     noOfMembers: "Provider Group 10",
  //     topMembers: 13.4,

  //   },
  // ];

  // const [membersbyProviderRowData, setMembersbyProviderRowData] = useState(
  //   membersbyProviderData
  // );

  // const membersbyProviderColDefs = [
  //   {
  //     field: "noOfMembers",
  //     headerName: "No. of Members",
  //     flex: 1,
  //   },
  //   {
  //     field: "topMembers",
  //     headerName: "Top Members",
  //     flex: 1,
  //     hide: showGraph,
  //   },
  //   {
  //     field: "topMembersGraph",
  //     headerName: "Top Members",
  //     flex: 1,
  //     cellRenderer: handleGraph,
  //     hide: !showGraph,
  //   },
  // ];

  //Top 10  Specialties
  const specialtiesData = [
    {
      specialties: "Sample Long Provider Name",
      cost: "10",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "9",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "8",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "7",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "6",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "5",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "4",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "3",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "2",
    },
    {
      specialties: "Sample Long Provider Name",
      cost: "1",
    },
  ];
  // const [specialtiesRowData, setSpecialtiesRowData] = useState(specialtiesData);
  // const specialtiesColDefs = [
  //   {
  //     field: "specialties",
  //     headerName: "Top Providers",
  //     flex: 1,
  //     hide: showGraph,
  //   },
  //   {
  //     field: "specialties",
  //     headerName: "Top Providers",
  //     flex: 1,
  //     hide: !showGraph,
  //     cellRenderer: handleGraph,
  //   },
  // ];
  const handleTableView = () => {
    setShowGraph(!showGraph);
  };
  return (
    <>
      <span className="d-flex justify-content-between mt-2">
        <h6>Top 10 Members & Providers per Care Provider</h6>
        <span className="d-flex justify-content-end">
          {/* <p>PCP</p>
          <span className="mx-1">{vector}</span>
          <span>
            <InputSelectField
              options={[{ label: "Surgery", value: "" }]}
            ></InputSelectField>
          </span> */}
          <div onClick={handleTableView} className="coc-cursor ms-3">
            View: {showGraph ? graphSVG : "123"}
          </div>
        </span>
      </span>
      <div className="d-flex mt-0 justify-content-between careProviderDetails">
        <div className="col-7 p-2">
          {/* <div
            className={"ag-theme-quartz"}
            style={{
              height: "459px",
            }}
          >
            <AgGridReact
              rowData={membersbyProviderRowData}
              columnDefs={membersbyProviderColDefs}
            />
          </div> */}
          <table className="table table-bordered border-2 rounded">
            <tr className="border">
              <th>No of Members</th>
              <th>Top Members</th>
            </tr>
            <tbody>
              {specialtiesData.map((item, index) => (
                <tr key={index}>
                  {index === 0 && (
                    <td
                      rowSpan={specialtiesData.length}
                      className="align-middle"
                    >
                      13k
                    </td>
                  )}
                  <td>
                    {showGraph ? (
                      <div className="d-flex">
                        <span>{item.specialties}</span>
                        <HorizontalBarChart
                          actualvalue={item.cost}
                          type={"number"}
                          aspectRatio={5}
                          graphLength={1}
                          height={"35px"}
                        />
                      </div>
                    ) : (
                      `${item.specialties} - ${item.cost}`
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="col-5 p-2">
          {/* <div
            className={"ag-theme-quartz"}
            style={{
              height: "459px",
            }}
          >
            <AgGridReact
              rowData={specialtiesRowData}
              columnDefs={specialtiesColDefs}
            />
          </div> */}
          <table className="table table-bordered border-2 rounded">
            <tr className="border">
              <th>Top Providers</th>
            </tr>
            <tbody>
              {specialtiesData.map((item, index) => (
                <tr key={index}>
                  {/* {index === 0 && (
                      <td rowSpan={specialtiesData.length} className="align-middle">
                        13k
                      </td>
                    )} */}
                  <td>
                    {showGraph ? (
                      <div className="d-flex">
                        <span>{item.specialties}</span>
                        <HorizontalBarChart
                          actualvalue={item.cost}
                          type={"number"}
                          aspectRatio={5}
                          graphLength={1}
                          height={"35px"}
                        />
                      </div>
                    ) : (
                      `${item.specialties} - ${item.cost}`
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};
export default CareProviderDetails;
