export const formInputValues = {
  lob: "",
  state: "",
  product: "",
  amountType: "",
  endMonth: "Feb 2021",
  graphType: "",
  startMonth: "",
};

export const formSelectedValues = {
  lob: [{ value: "" }],
  state: [{ value: "" }],
  product: [{ value: "" }],
  amountType: [{ value: "Paid" }],
  endMonth: [{ value: "" }],
  graphType: [{ value: "" }],
  startMonth: "",
};

export const formOptions = {
  lob: [{ label: "All", value: "" }],
  state: [{ label: "All", value: "" }],
  product: [{ label: "All", value: "All" }],
  amountType: [{ label: "Paid", value: "Paid" }],
  endMonth: [{ label: "", value: "" }],
  graphType: [
    { label: "Target vs Actual", value: "Target vs Actual" },
    { label: "Current vs Prior", value: "Current vs Prior" },
  ],
};

export const kpiMetricsconst = {
  memberMonths: 0,
  PMPMExpense: 0,
  endingMembers: 0,
  targetMemberMonths: 0,
  targetPMPMExpense: 0,
  targetEndingMembers: 0,
  memberMonthsPercentageChange: 0,
  endingMembersPercentageChange: 0,
  pmpmPercentageChange: 0,
};

export const radioButtonOptions = [
  { label: "M", value: 0 },
  { label: "3M", value: 2 },
  { label: "6M", value: 5 },
  { label: "YTD", value: 6 },
  { label: "1Y", value: 11 },
];

export const monthMapping = {
  M: 0,
  "3M": 2,
  "6M": 5,
  YTD: 6,
  "1Y": 11,
};

export const targetVsActulaGraphtype = {
  type1: "Target",
  type2: "Actual",
};

export const currentVsPriorGraphtype = {
  type1: "Prior",
  type2: "Current",
};

export const navLinks = [
  {
    title: "Summary",
    url: "/summaryPage",
  },
  {
    title: "Care Category ",
    url: "/careCategory",
  },
  {
    title: "Service Region ",
    url: "/serviceRegion",
  },

  {
    title: "Provider Specialty ",
    url: "/providerSpecialty",
  },
  {
    title: "Care Provider ",
    url: "/careProvider",
  },

  {
    title: "PCP Group ",
    url: "/pcpGroup",
  },
  {
    title: "Forecast ",
    url: "/foreCast",
  },
];
