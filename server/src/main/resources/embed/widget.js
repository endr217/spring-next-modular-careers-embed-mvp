(function () {
  const script = document.currentScript;
  const websiteSlug = script.getAttribute('data-website-slug');
  const config = {
    brandingColor: script.getAttribute('data-branding-color') || __BRANDING_COLOR__ || '#4CAF50',
    fontFamily: script.getAttribute('data-font-family') || __FONT_FAMILY__ || 'sans-serif',
    jobsEndpoint: __JOBS_ENDPOINT__,
    applyEndpoint: __APPLY_ENDPOINT__,
    containerId: 'job-widget-' + websiteSlug,
  };

  function container() {
    let element = document.getElementById(config.containerId);
    if (!element) {
      element = document.createElement('div');
      element.id = config.containerId;
      script.parentNode.insertBefore(element, script);
    }
    return element;
  }

  function loadStyles() {
    const id = '#' + config.containerId;
    const style = document.createElement('style');
    style.textContent = [
      id + ' { font-family: ' + config.fontFamily + '; max-width: 800px; margin: 0 auto; padding: 20px; }',
      id + ' .job-card { border: 1px solid #ddd; border-radius: 5px; padding: 15px; margin-bottom: 15px; background-color: #f9f9f9; }',
      id + ' .apply-btn { background-color: ' + config.brandingColor + '; color: #fff; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }',
      id + ' .loading { text-align: center; padding: 20px; }',
      id + ' .form-error { color: #b00020; margin: 10px 0; }',
    ].join('\n');
    document.head.appendChild(style);
  }

  async function fetchJobs() {
    const root = container();
    root.innerHTML = '<div class="loading">Loading job listings...</div>';
    try {
      const response = await fetch(config.jobsEndpoint);
      if (!response.ok) throw new Error('Request failed with status ' + response.status);
      root.innerHTML = await response.text();
      bindEvents();
    } catch (error) {
      console.error('Error loading jobs:', error);
      root.innerHTML = '<p>Error loading job listings. Please try again later.</p>';
    }
  }

  function bindEvents() {
    const root = container();
    const backButton = root.querySelector('.back-btn');
    if (backButton) backButton.addEventListener('click', fetchJobs);

    root.querySelectorAll('.apply-btn[data-job-id]').forEach(function (button) {
      button.addEventListener('click', function () {
        showForm(button.getAttribute('data-job-id'));
      });
    });

    const form = root.querySelector('#application-form');
    if (form) form.addEventListener('submit', submit);
  }

  function showForm(jobId) {
    const root = container();
    const list = root.querySelector('#job-list-container');
    const formContainer = root.querySelector('#application-form-container');
    const title = root.querySelector('#job-' + jobId + ' h3').textContent;
    if (list) list.style.display = 'none';
    if (!formContainer) return;
    formContainer.style.display = 'block';
    formContainer.querySelector('#apply-job-title').textContent = 'Apply for: ' + title;
    formContainer.querySelector('#application-form').reset();
    formContainer.querySelector('#job-id').value = jobId;
    formContainer.querySelector('#success-message').style.display = 'none';
    formContainer.querySelector('#form-error').textContent = '';
  }

  async function submit(event) {
    event.preventDefault();
    const root = container();
    const form = event.target;
    const button = root.querySelector('#submit-btn');
    const error = root.querySelector('#form-error');
    error.textContent = '';
    button.disabled = true;
    button.textContent = 'Submitting...';
    try {
      const response = await fetch(config.applyEndpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
        body: JSON.stringify({
          job_posting_id: Number(form.querySelector('#job-id').value),
          name: form.querySelector('#name').value,
          email: form.querySelector('#email').value,
          motivation_text: form.querySelector('#motivation_text').value,
          cv_file_path: '/uploads/cvs/default_cv.pdf',
        }),
      });
      if (response.ok) {
        root.querySelector('#success-message').style.display = 'block';
        form.reset();
      } else {
        const body = await response.json().catch(function () { return {}; });
        error.textContent = body.message || 'Error submitting application';
      }
    } catch (failure) {
      console.error('Error:', failure);
      error.textContent = 'Error submitting application';
    } finally {
      button.disabled = false;
      button.textContent = 'Submit Application';
    }
  }

  function init() {
    container();
    loadStyles();
    fetchJobs();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
