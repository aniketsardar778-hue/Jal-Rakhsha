const BASE_URL = "https://jal-rakhsha-1.onrender.com";
let allLocations = [];
let latestAssessment = null;

async function fetchAPI(path, options = {}) {
    const response = await fetch(BASE_URL + path, {
        ...options,
        headers: { "Content-Type": "application/json", ...(options.headers || {}) }
    });
    const type = response.headers.get("content-type") || "";
    const data = type.includes("application/json") ? await response.json() : await response.text();
    if (!response.ok) {
        throw new Error(data?.message || data?.error || (typeof data === "string" ? data : "Request failed"));
    }
    return data;
}

async function getAllLocations() { return fetchAPI("/locations"); }

async function loadLocationOptions() {
    const select = document.getElementById("locationId");
    try {
        allLocations = await getAllLocations();
        select.innerHTML = '<option value="">Select Location</option>';
        allLocations.forEach(l => {
            const option = document.createElement("option");
            option.value = l.id;
            option.textContent = `${l.locationName}, ${l.district}, ${l.state}`;
            select.appendChild(option);
        });
        if (!allLocations.length) {
            document.getElementById("selectedLocationInfo").style.display = "block";
            document.getElementById("selectedLocationInfo").innerHTML = "<strong>No environmental location data is stored yet.</strong><br>Add real LocationData using POST /locations before running an assessment.";
        }
    } catch (e) {
        console.error(e);
        document.getElementById("gpsStatus").textContent = "Could not load LocationData. Make sure Spring Boot is running.";
    }
}

async function selectNearestStoredLocation(lat, lng) {
    const location = await fetchAPI(`/locations/nearest?latitude=${encodeURIComponent(lat)}&longitude=${encodeURIComponent(lng)}`);
    const select = document.getElementById("locationId");
    if (![...select.options].some(o => String(o.value) === String(location.id))) await loadLocationOptions();
    select.value = String(location.id);
    select.dispatchEvent(new Event("change"));
    return location;
}

function showSelectedLocation(location) {
    const box = document.getElementById("selectedLocationInfo");
    if (!location) { box.style.display = "none"; return; }
    box.style.display = "block";
    box.innerHTML = `<strong>${escapeHtml(location.locationName)}</strong>, ${escapeHtml(location.district)}, ${escapeHtml(location.state)}<br>Environmental data loaded from database: Rainfall ${location.annualRainfall ?? "N/A"} mm, Soil ${escapeHtml(location.soilType ?? "N/A")}, Groundwater ${location.groundWaterDepth ?? "N/A"} m.`;
}

document.getElementById("locationId").addEventListener("change", () => {
    const id = document.getElementById("locationId").value;
    const location = allLocations.find(l => String(l.id) === String(id));
    showSelectedLocation(location);
});

async function submitAssessment() {
    const payload = {
        userName: document.getElementById("username").value.trim(),
        locationId: Number(document.getElementById("locationId").value),
        roofArea: Number(document.getElementById("roofArea").value),
        roofType: document.getElementById("roofType").value,
        dwellers: Number(document.getElementById("dwellers").value),
        openSpace: Number(document.getElementById("openSpace").value || 0)
    };
    if (!payload.locationId) { alert("Please select a location or use current location first."); return; }
    try {
        showLoading();
        const data = await fetchAPI("/assessments/generate", { method: "POST", body: JSON.stringify(payload) });
        latestAssessment = data;
        localStorage.setItem("latestReport", JSON.stringify(data));
        renderReport(data);
        document.getElementById("results").scrollIntoView({ behavior: "smooth" });
    } catch (e) { alert(e.message); }
    finally { hideLoading(); }
}

document.getElementById("assessmentForm").addEventListener("submit", e => { e.preventDefault(); submitAssessment(); });

function renderReport(d) {
    document.getElementById("reportSection").style.display = "block";
    const map = {
        assessmentId:d.assessmentId, reportUsername:d.username, createdAt:d.createdAt,
        locationName:d.locationName, district:d.district, state:d.state, latitude:d.latitude, longitude:d.longitude,
        annualRainfall:d.annualRainfall, groundwaterDepth:d.groundwaterDepth, soilType:d.soilType, aquiferType:d.aquiferType, rechargePotential:d.rechargePotential,
        reportRoofArea:d.roofArea, reportRoofType:d.roofType, reportDwellers:d.dwellers, reportOpenSpace:d.openSpace,
        runoffCoefficient:d.runoffCoefficient, harvestPotential:d.annualHarvestPotential, waterDemand:d.annualWaterDemand,
        waterSavingPercentage:d.waterSavingPercentage, annualSurplus:d.annualSurplus, annualDeficit:d.annualDeficit, rechargeVolume:d.rechargeVolume,
        rainfallScore:d.rainfallScore, roofAreaScore:d.roofAreaScore, roofTypeScore:d.roofTypeScore, demandCoverageScore:d.demandCoverageScore,
        collectionEfficiencyScore:d.collectionEfficiencyScore, rtrwhScore:d.rtrwhScore, rtrwhStatus:d.rtrwhStatus,
        soilScore:d.soilScore, openSpaceScore:d.openSpaceScore, aquiferSuitabilityScore:d.aquiferSuitabilityScore,
        groundwaterScore:d.groundwaterScore, rechargePotentialScore:d.rechargePotentialScore, rechargeScore:d.rechargeScore, rechargeStatus:d.rechargeStatus,
        overallScore:d.overallScore, overallStatus:d.overallStatus,
        recommendedStructure:d.recommendedStructure, recommendedDimensions:d.recommendedDimensions, recommendedCapacity:d.recommendedCapacity,
        recommendationSuitabilityScore:d.recommendationSuitabilityScore, recommendationReason:d.recommendationReason,
        materialCost:formatCurrency(d.materialCost), labourCost:formatCurrency(d.labourCost), excavationCost:formatCurrency(d.excavationCost),
        maintenanceCost:formatCurrency(d.maintenanceCost), totalCost:formatCurrency(d.totalCost), annualBenefit:formatCurrency(d.annualBenefit),
        paybackPeriodYears:d.paybackPeriodYears, roiPercentage:d.roiPercentage
    };
    Object.entries(map).forEach(([id, value]) => setText(id, value));
    document.getElementById("rainfall").textContent = `${d.annualRainfall ?? 0} mm`;
    document.getElementById("dashRoofArea").textContent = `${d.roofArea ?? 0} m²`;
    document.getElementById("waterPotential").textContent = `${formatNumber(d.annualHarvestPotential)} L`;
    document.getElementById("score").textContent = `${d.overallScore ?? 0}%`;
}

async function loadReport(id) {
    try { showLoading(); const d = await fetchAPI(`/report/${id}`); latestAssessment = d; localStorage.setItem("latestReport", JSON.stringify(d)); renderReport(d); document.getElementById("results").scrollIntoView({behavior:"smooth"}); }
    catch(e) { alert(e.message); } finally { hideLoading(); }
}

async function loadHistory() {
    const username = document.getElementById("historyUsername").value.trim();
    if (!username) { alert("Enter username."); return; }
    try {
        showLoading();
        const history = await fetchAPI(`/assessments-History/user/${encodeURIComponent(username)}`);
        const container = document.getElementById("assessmentHistory");
        container.innerHTML = "";
        if (!history.length) { container.innerHTML = "<p>No assessment history found.</p>"; return; }
        history.forEach(a => {
            const div = document.createElement("div");
            div.className = "assessment-card";
            div.innerHTML = `<h3>Assessment #${a.assessmentId}</h3><p>Location: ${escapeHtml(a.locationName)}</p><p>Roof Area: ${a.roofArea} m²</p><p>Overall Score: ${a.overallScore}/100</p><p>Status: ${escapeHtml(a.overallStatus)}</p><button type="button">View Complete Report</button>`;
            div.querySelector("button").addEventListener("click", () => loadReport(a.assessmentId));
            container.appendChild(div);
        });
    } catch(e) { alert(e.message); } finally { hideLoading(); }
}

document.getElementById("loadHistoryBtn").addEventListener("click", loadHistory);

async function handleDownloadReport() {
    let report = latestAssessment;
    if (!report) {
        const stored = localStorage.getItem("latestReport");
        if (stored) report = JSON.parse(stored);
    }
    if (!report?.assessmentId) { alert("Run an assessment first."); return; }
    try {
        showLoading();
        await fetchAPI(`/report/${report.assessmentId}/report?language=en`, { method: "POST" });
        const complete = await fetchAPI(`/report/${report.assessmentId}`);
        renderReport(complete);
        window.print();
    } catch(e) { alert(e.message); } finally { hideLoading(); }
}
document.getElementById("downloadBtn").addEventListener("click", handleDownloadReport);

function setText(id, value) { const el = document.getElementById(id); if (el) el.textContent = value ?? "N/A"; }
function formatNumber(value) { return value == null ? "0" : Number(value).toLocaleString("en-IN"); }
function formatCurrency(value) { return value == null ? "N/A" : Number(value).toLocaleString("en-IN", {style:"currency", currency:"INR", maximumFractionDigits:2}); }
function escapeHtml(value) { return String(value ?? "").replace(/[&<>'"]/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;","'":"&#39;",'"':"&quot;"}[c])); }
function showLoading() { const e=document.getElementById("loading"); if(e)e.style.display="flex"; }
function hideLoading() { const e=document.getElementById("loading"); if(e)e.style.display="none"; }

document.addEventListener("DOMContentLoaded", loadLocationOptions);

