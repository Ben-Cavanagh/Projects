// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GhoulSpawner : MonoBehaviour
{
    // Define ghoul-related variables
    [SerializeField] private GhoulMove ghoul; 
    [Range(1,10)] [SerializeField] private float ghoulSpeed = 2;
    [SerializeField] private Path path;

    // Define timer-related variables
    [Range(1,10)] [SerializeField] private int minTimeDelay = 3;
    [Range(1,10)] [SerializeField] private int maxTimeDelay = 6;
    private float timer; 

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void Start()
    {
        // Set ghoul spawn to a random time
        timer = Random.Range(minTimeDelay, maxTimeDelay);
    }

    private void Update()
    {
        // Update value of timer and spawn when time is zero
        timer -= Time.deltaTime;
        if (timer <= 0f)
        {
            Spawn();
        }
    }

    private void Spawn()
    {
        // Spawn new ghoul
        GhoulMove newGhoul = Instantiate(ghoul, transform.position, Quaternion.identity, transform);

        // Set ghoul properties
        newGhoul.Path = path;
        newGhoul.Speed = ghoulSpeed;
        newGhoul.LevelManager = levelManager;

        // Reset timer when it hits zero
        timer = Random.Range(minTimeDelay, maxTimeDelay);
    }
}