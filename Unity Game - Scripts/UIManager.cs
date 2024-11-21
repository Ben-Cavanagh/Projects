// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class UIManager : MonoBehaviour
{
    // Define UI text variables
    [SerializeField] private TextMeshProUGUI currentScoreText;
    [SerializeField] private TextMeshProUGUI highScoreText;
    [SerializeField] private TextMeshProUGUI timerText;

    // Define buttton
    [SerializeField] private Button resetButton;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void Start()
    {
        // Initialise button
        resetButton.onClick.AddListener(levelManager.Reset);
    }

    private void Update()
    {
        // Update the UI text
        currentScoreText.text = "Current Score: " + levelManager.CurrentScore;
        highScoreText.text = "High Score: " + levelManager.HighScore;
        timerText.text = "Time Left: " + levelManager.Timer;
    }
}
